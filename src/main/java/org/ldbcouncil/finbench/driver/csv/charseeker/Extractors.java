package org.ldbcouncil.finbench.driver.csv.charseeker;

import static java.lang.reflect.Modifier.isStatic;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Common implementations of {@link Extractor}. Since array values can have a delimiter of user choice this isn't
 * an enum, but a regular class with a constructor where that delimiter can be specified.
 * <p/>
 * The common {@link Extractor extractors} can be accessed using the accessor methods, like {@link #string()},
 * {@link #long_()} and others. Specific classes are declared as return types for those providing additional
 * value accessors
 * <p/>
 * Typically an instance of {@link Extractors} would be instantiated along side a {@link BufferedCharSeeker},
 * assumed to be used by a single thread, since each {@link Extractor} it has is stateful. Example:
 * <p/>
 * <pre>
 * CharSeeker seeker = ...
 * Mark mark = new Mark();
 * Extractors extractors = new Extractors( ';' );
 *
 * // ... seek a value, then extract like this
 * int boxFreeIntValue = seeker.extract( mark, extractors.int_() ).intValue();
 * // ... or using any other type of extractor.
 * </pre>
 * <p/>
 * Custom {@link Extractor extractors} can also be implemented and used, if need arises:
 * <p/>
 * <pre>
 * CharSeeker seeker = ...
 * Mark mark = new Mark();
 * MyStringDateToLongExtractor dateExtractor = new MyStringDateToLongExtractor();
 *
 * // ... seek a value, then extract like this
 * long timestamp = seeker.extract( mark, dateExtractor ).dateAsMillis();
 * </pre>
 * <p/>
 * ... even {@link Extractors#add(Extractor) added} to an {@link Extractors} instance, where its
 * {@link Extractor#toString() toString} value is used as key for lookup in {@link #valueOf(String)}.
 */
public class Extractors {
    private static final char[] BOOLEAN_MATCH;
    private static final char[] BOOLEAN_TRUE_CHARACTERS;

    static {
        BOOLEAN_MATCH = new char[Boolean.TRUE.toString().length()];
        Boolean.TRUE.toString().getChars(0, BOOLEAN_MATCH.length, BOOLEAN_MATCH, 0);
    }

    static {
        BOOLEAN_TRUE_CHARACTERS = new char[Boolean.TRUE.toString().length()];
        Boolean.TRUE.toString().getChars(0, BOOLEAN_TRUE_CHARACTERS.length, BOOLEAN_TRUE_CHARACTERS, 0);
    }

    private final Map<String, Extractor<?>> instances = new HashMap<>();
    private final Extractor<String> string;
    private final LongExtractor long_;
    private final IntExtractor int_;
    private final CharExtractor char_;
    private final ShortExtractor SIMPLE_;
    private final ByteExtractor byte_;
    private final BooleanExtractor boolean_;
    private final FloatExtractor float_;
    private final DoubleExtractor double_;
    private final Extractor<String[]> stringArray;
    private final Extractor<boolean[]> booleanArray;
    private final Extractor<byte[]> byteArray;
    private final Extractor<short[]> shortArray;
    private final Extractor<int[]> intArray;
    private final Extractor<long[]> longArray;
    private final Extractor<float[]> floatArray;
    private final Extractor<double[]> doubleArray;
    private final IntTupleArrayExtractor intTupleArray;

    /**
     * Why do we have a public constructor here and why isn't this class an enum?
     * It's because the array extractors can be configured with an array delimiter,
     * something that would be impossible otherwise. There's an equivalent {@link #valueOf(String)}
     * method to keep the feel of an enum.
     */
    public Extractors(char arrayDelimiter, char tupleDelimiter) {
        try {
            for (Field field : getClass().getDeclaredFields()) {
                if (isStatic(field.getModifiers())) {
                    Object value = field.get(null);
                    if (value instanceof Extractor) {
                        instances.put(field.getName(), (Extractor<?>) value);
                    }
                }
            }

            add(string = new StringExtractor());
            add(long_ = new LongExtractor());
            add(int_ = new IntExtractor());
            add(char_ = new CharExtractor());
            add(SIMPLE_ = new ShortExtractor());
            add(byte_ = new ByteExtractor());
            add(boolean_ = new BooleanExtractor());
            add(float_ = new FloatExtractor());
            add(double_ = new DoubleExtractor());
            add(stringArray = new StringArrayExtractor(arrayDelimiter));
            add(booleanArray = new BooleanArrayExtractor(arrayDelimiter));
            add(byteArray = new ByteArrayExtractor(arrayDelimiter));
            add(shortArray = new ShortArrayExtractor(arrayDelimiter));
            add(intArray = new IntArrayExtractor(arrayDelimiter));
            add(longArray = new LongArrayExtractor(arrayDelimiter));
            add(floatArray = new FloatArrayExtractor(arrayDelimiter));
            add(doubleArray = new DoubleArrayExtractor(arrayDelimiter));
            add(intTupleArray = new IntTupleArrayExtractor(arrayDelimiter, tupleDelimiter));
        } catch (IllegalAccessException e) {
            throw new Error("Bug in reflection code gathering all extractors");
        }
    }

    private static long extractLong(char[] data, int offset, int length) {
        if (length == 0) {
            throw new NumberFormatException("For input string \"" + String.valueOf(data, offset, length) + "\"");
        }

        long result = 0;
        int i = 0;
        boolean negate = false;
        if (data[offset] == '-') {
            negate = true;
            i++;
        }
        for (; i < length; i++) {
            result = result * 10 + digit(data[offset + i]);
        }
        return negate ? -result : result;
    }

    private static int digit(char ch) {
        int digit = ch - '0';
        if ((digit < 0) || (digit > 9)) {
            throw new NumberFormatException(
                "Invalid digit character " + digit + " '" + (char) digit + "' where the original char was '" + ch
                    + "'");
        }
        return digit;
    }

    private static boolean extractBoolean(char[] data, int offset, int length) {
        if (BOOLEAN_TRUE_CHARACTERS.length != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (data[offset + i] != BOOLEAN_TRUE_CHARACTERS[i]) {
                return false;
            }
        }
        return true;
    }

    private static int safeCastLongToInt(long value) {
        if (value > Integer.MAX_VALUE) {
            throw new UnsupportedOperationException("Not supported a.t.m");
        }
        return (int) value;
    }

    private static short safeCastLongToShort(long value) {
        if (value > Short.MAX_VALUE) {
            throw new UnsupportedOperationException("Not supported a.t.m");
        }
        return (short) value;
    }

    private static byte safeCastLongToByte(long value) {
        if (value > Byte.MAX_VALUE) {
            throw new UnsupportedOperationException("Not supported a.t.m");
        }
        return (byte) value;
    }

    public void add(Extractor<?> extractor) {
        instances.put(extractor.toString().toUpperCase(), extractor);
    }

    public Extractor<?> valueOf(String name) {
        Extractor<?> instance = instances.get(name.toUpperCase());
        if (instance == null) {
            throw new IllegalArgumentException("'" + name + "'");
        }
        return instance;
    }

    public Extractor<String> string() {
        return string;
    }

    public LongExtractor long_() {
        return long_;
    }

    public IntExtractor int_() {
        return int_;
    }

    public CharExtractor char_() {
        return char_;
    }

    public ShortExtractor SIMPLE_() {
        return SIMPLE_;
    }

    public ByteExtractor byte_() {
        return byte_;
    }

    public BooleanExtractor boolean_() {
        return boolean_;
    }

    public FloatExtractor float_() {
        return float_;
    }

    public DoubleExtractor double_() {
        return double_;
    }

    public Extractor<String[]> stringArray() {
        return stringArray;
    }

    public Extractor<boolean[]> booleanArray() {
        return booleanArray;
    }

    public Extractor<byte[]> byteArray() {
        return byteArray;
    }

    public Extractor<short[]> shortArray() {
        return shortArray;
    }

    public Extractor<int[]> intArray() {
        return intArray;
    }

    public Extractor<long[]> longArray() {
        return longArray;
    }

    public Extractor<float[]> floatArray() {
        return floatArray;
    }

    public Extractor<double[]> doubleArray() {
        return doubleArray;
    }

    public Extractor<int[][]> intTupleArray(int tupleLength) {
        intTupleArray.setInnerTupleLength(tupleLength);
        return intTupleArray;
    }

    private abstract static class AbstractExtractor<T> implements Extractor<T> {
        private final String toString;

        AbstractExtractor(String toString) {
            this.toString = toString;
        }

        @Override
        public String toString() {
            return toString;
        }
    }

    private static class StringExtractor extends AbstractExtractor<String> {
        private String value;

        StringExtractor() {
            super(String.class.getSimpleName());
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            value = length > 0 ? new String(data, offset, length) : null;
        }

        @Override
        public String value() {
            return value;
        }
    }

    public static class LongExtractor extends AbstractExtractor<Long> {
        private long value;

        LongExtractor() {
            super(Long.TYPE.getSimpleName());
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            value = extractLong(data, offset, length);
        }

        @Override
        public Long value() {
            return Long.valueOf(value);
        }

        /**
         * Value accessor bypassing boxing.
         *
         * @return the number value in its primitive form.
         */
        public long longValue() {
            return value;
        }
    }

    public static class IntExtractor extends AbstractExtractor<Integer> {
        private int value;

        IntExtractor() {
            super(Integer.TYPE.toString());
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            value = safeCastLongToInt(extractLong(data, offset, length));
        }

        @Override
        public Integer value() {
            return Integer.valueOf(value);
        }

        /**
         * Value accessor bypassing boxing.
         *
         * @return the number value in its primitive form.
         */
        public int intValue() {
            return value;
        }
    }

    public static class ShortExtractor extends AbstractExtractor<Short> {
        private short value;

        ShortExtractor() {
            super(Short.TYPE.getSimpleName());
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            value = safeCastLongToShort(extractLong(data, offset, length));
        }

        @Override
        public Short value() {
            return Short.valueOf(value);
        }

        /**
         * Value accessor bypassing boxing.
         *
         * @return the number value in its primitive form.
         */
        public short shortValue() {
            return value;
        }
    }

    public static class ByteExtractor extends AbstractExtractor<Byte> {
        private byte value;

        ByteExtractor() {
            super(Byte.TYPE.getSimpleName());
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            value = safeCastLongToByte(extractLong(data, offset, length));
        }

        @Override
        public Byte value() {
            return Byte.valueOf(value);
        }

        /**
         * Value accessor bypassing boxing.
         *
         * @return the number value in its primitive form.
         */
        public int byteValue() {
            return value;
        }
    }

    public static class BooleanExtractor extends AbstractExtractor<Boolean> {
        private boolean value;

        BooleanExtractor() {
            super(Boolean.TYPE.getSimpleName());
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            value = extractBoolean(data, offset, length);
        }

        @Override
        public Boolean value() {
            return Boolean.valueOf(value);
        }

        public boolean booleanValue() {
            return value;
        }
    }

    public static class CharExtractor extends AbstractExtractor<Character> {
        private char value;

        CharExtractor() {
            super(Character.TYPE.getSimpleName());
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            if (length != 1) {
                throw new IllegalStateException("Was told to extract a character, but length:" + length);
            }
            value = data[offset];
        }

        @Override
        public Character value() {
            return Character.valueOf(value);
        }

        public char charValue() {
            return value;
        }
    }

    public static class FloatExtractor extends AbstractExtractor<Float> {
        private float value;

        FloatExtractor() {
            super(Float.TYPE.getSimpleName());
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            // TODO Figure out a way to do this conversion without round tripping to String
            value = Float.parseFloat(String.valueOf(data, offset, length));
        }

        @Override
        public Float value() {
            return Float.valueOf(value);
        }

        public float floatValue() {
            return value;
        }
    }

    public static class DoubleExtractor extends AbstractExtractor<Double> {
        private double value;

        DoubleExtractor() {
            super(Double.TYPE.getSimpleName());
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            // TODO Figure out a way to do this conversion without round tripping to String
            value = Double.parseDouble(String.valueOf(data, offset, length));
        }

        @Override
        public Double value() {
            return Double.valueOf(value);
        }

        public double doubleValue() {
            return value;
        }
    }

    private abstract static class ArrayExtractor<T> extends AbstractExtractor<T> {
        protected final char arrayDelimiter;
        protected T value;

        ArrayExtractor(char arrayDelimiter, Class<?> componentType) {
            super(componentType.getSimpleName() + "[]");
            this.arrayDelimiter = arrayDelimiter;
        }

        @Override
        public T value() {
            return value;
        }

        protected int charsToNextDelimiter(char[] data, int offset, int length) {
            for (int i = 0; i < length; i++) {
                if (data[offset + i] == arrayDelimiter) {
                    return i;
                }
            }
            return length;
        }

        protected int numberOfValues(char[] data, int offset, int length) {
            int count = length > 0 ? 1 : 0;
            for (int i = 0; i < length; i++) {
                if (data[offset + i] == arrayDelimiter) {
                    count++;
                }
            }
            return count;
        }

        @Override
        public int hashCode() {
            return getClass().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return getClass().equals(obj.getClass());
        }
    }

    private static class StringArrayExtractor extends ArrayExtractor<String[]> {
        private static final String[] EMPTY = new String[0];

        StringArrayExtractor(char arrayDelimiter) {
            super(arrayDelimiter, String.class);
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            int numberOfValues = numberOfValues(data, offset, length);
            value = numberOfValues > 0 ? new String[numberOfValues] : EMPTY;
            for (int arrayIndex = 0, charIndex = 0; arrayIndex < numberOfValues; arrayIndex++, charIndex++) {
                int numberOfChars = charsToNextDelimiter(data, offset + charIndex, length - charIndex);
                value[arrayIndex] = new String(data, offset + charIndex, numberOfChars);
                charIndex += numberOfChars;
            }
        }
    }

    private static class ByteArrayExtractor extends ArrayExtractor<byte[]> {
        private static final byte[] EMPTY = new byte[0];

        ByteArrayExtractor(char arrayDelimiter) {
            super(arrayDelimiter, Byte.TYPE);
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            int numberOfValues = numberOfValues(data, offset, length);
            value = numberOfValues > 0 ? new byte[numberOfValues] : EMPTY;
            for (int arrayIndex = 0, charIndex = 0; arrayIndex < numberOfValues; arrayIndex++, charIndex++) {
                int numberOfChars = charsToNextDelimiter(data, offset + charIndex, length - charIndex);
                value[arrayIndex] = safeCastLongToByte(extractLong(data, offset + charIndex, numberOfChars));
                charIndex += numberOfChars;
            }
        }
    }

    private static class ShortArrayExtractor extends ArrayExtractor<short[]> {
        private static final short[] EMPTY = new short[0];

        ShortArrayExtractor(char arrayDelimiter) {
            super(arrayDelimiter, Short.TYPE);
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            int numberOfValues = numberOfValues(data, offset, length);
            value = numberOfValues > 0 ? new short[numberOfValues] : EMPTY;
            for (int arrayIndex = 0, charIndex = 0; arrayIndex < numberOfValues; arrayIndex++, charIndex++) {
                int numberOfChars = charsToNextDelimiter(data, offset + charIndex, length - charIndex);
                value[arrayIndex] = safeCastLongToShort(extractLong(data, offset + charIndex, numberOfChars));
                charIndex += numberOfChars;
            }
        }
    }

    private static class IntArrayExtractor extends ArrayExtractor<int[]> {
        private static final int[] EMPTY = new int[0];

        IntArrayExtractor(char arrayDelimiter) {
            super(arrayDelimiter, Integer.TYPE);
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            int numberOfValues = numberOfValues(data, offset, length);
            value = numberOfValues > 0 ? new int[numberOfValues] : EMPTY;
            for (int arrayIndex = 0, charIndex = 0; arrayIndex < numberOfValues; arrayIndex++, charIndex++) {
                int numberOfChars = charsToNextDelimiter(data, offset + charIndex, length - charIndex);
                value[arrayIndex] = safeCastLongToInt(extractLong(data, offset + charIndex, numberOfChars));
                charIndex += numberOfChars;
            }
        }
    }

    private static class LongArrayExtractor extends ArrayExtractor<long[]> {
        private static final long[] EMPTY = new long[0];

        LongArrayExtractor(char arrayDelimiter) {
            super(arrayDelimiter, Long.TYPE);
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            int numberOfValues = numberOfValues(data, offset, length);
            value = numberOfValues > 0 ? new long[numberOfValues] : EMPTY;
            for (int arrayIndex = 0, charIndex = 0; arrayIndex < numberOfValues; arrayIndex++, charIndex++) {
                int numberOfChars = charsToNextDelimiter(data, offset + charIndex, length - charIndex);
                value[arrayIndex] = extractLong(data, offset + charIndex, numberOfChars);
                charIndex += numberOfChars;
            }
        }
    }

    private static class IntTupleArrayExtractor extends AbstractExtractor<int[][]> {
        private static final int[][] EMPTY = new int[0][0];

        protected final char outerArrayDelimiter;
        protected final char innerTupleDelimiter;
        protected int innerTupleLength;
        protected int[][] value;

        IntTupleArrayExtractor(char outerArrayDelimiter, char innerTupleDelimiter) {
            super(Integer.TYPE + "[][]");
            this.outerArrayDelimiter = outerArrayDelimiter;
            this.innerTupleDelimiter = innerTupleDelimiter;
        }

        void setInnerTupleLength(int tupleLength) {
            innerTupleLength = tupleLength;
        }

        @Override
        public int[][] value() {
            return value;
        }

        protected int charsToNextOuterArrayDelimiter(char[] data, int offset, int length) {
            for (int i = 0; i < length; i++) {
                if (data[offset + i] == outerArrayDelimiter) {
                    return i;
                }
            }
            return length;
        }

        protected int charsToNextInnerTupleDelimiter(char[] data, int offset, int length) {
            for (int i = 0; i < length; i++) {
                if (data[offset + i] == innerTupleDelimiter) {
                    return i;
                }
            }
            return length;
        }

        protected int numberOfValues(char[] data, int offset, int length) {
            int count = length > 0 ? 1 : 0;
            for (int i = 0; i < length; i++) {
                if (data[offset + i] == outerArrayDelimiter) {
                    count++;
                }
            }
            return count;
        }

        @Override
        public int hashCode() {
            return getClass().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return getClass().equals(obj.getClass());
        }


        @Override
        public void extract(char[] data, int offset, int length) {
            int numberOfValues = numberOfValues(data, offset, length);
            value = numberOfValues > 0 ? new int[numberOfValues][innerTupleLength] : EMPTY;
            for (int arrayIndex = 0, charIndex = 0; arrayIndex < numberOfValues; arrayIndex++, charIndex++) {
                int numberOfChars = charsToNextOuterArrayDelimiter(data, offset + charIndex, length - charIndex);
                extractInnerTuple(data, offset + charIndex, numberOfChars, arrayIndex);
                charIndex += numberOfChars;
            }
        }

        private void extractInnerTuple(char[] data, int offset, int length, int outerArrayIndex) {
            for (int innerTupleIndex = 0, charIndex = 0; innerTupleIndex < innerTupleLength;
                 innerTupleIndex++, charIndex++) {
                int numberOfChars = charsToNextInnerTupleDelimiter(data, offset + charIndex, length - charIndex);
                value[outerArrayIndex][innerTupleIndex] =
                    safeCastLongToInt(extractLong(data, offset + charIndex, numberOfChars));
                charIndex += numberOfChars;
            }
        }
    }

    private static class FloatArrayExtractor extends ArrayExtractor<float[]> {
        private static final float[] EMPTY = new float[0];

        FloatArrayExtractor(char arrayDelimiter) {
            super(arrayDelimiter, Float.TYPE);
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            int numberOfValues = numberOfValues(data, offset, length);
            value = numberOfValues > 0 ? new float[numberOfValues] : EMPTY;
            for (int arrayIndex = 0, charIndex = 0; arrayIndex < numberOfValues; arrayIndex++, charIndex++) {
                int numberOfChars = charsToNextDelimiter(data, offset + charIndex, length - charIndex);
                // TODO Figure out a way to do this conversion without round tripping to String
                value[arrayIndex] = Float.parseFloat(String.valueOf(data, offset + charIndex, numberOfChars));
                charIndex += numberOfChars;
            }
        }
    }

    private static class DoubleArrayExtractor extends ArrayExtractor<double[]> {
        private static final double[] EMPTY = new double[0];

        DoubleArrayExtractor(char arrayDelimiter) {
            super(arrayDelimiter, Double.TYPE);
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            int numberOfValues = numberOfValues(data, offset, length);
            value = numberOfValues > 0 ? new double[numberOfValues] : EMPTY;
            for (int arrayIndex = 0, charIndex = 0; arrayIndex < numberOfValues; arrayIndex++, charIndex++) {
                int numberOfChars = charsToNextDelimiter(data, offset + charIndex, length - charIndex);
                // TODO Figure out a way to do this conversion without round tripping to String
                value[arrayIndex] = Double.parseDouble(String.valueOf(data, offset + charIndex, numberOfChars));
                charIndex += numberOfChars;
            }
        }
    }

    private static class BooleanArrayExtractor extends ArrayExtractor<boolean[]> {
        private static final boolean[] EMPTY = new boolean[0];

        BooleanArrayExtractor(char arrayDelimiter) {
            super(arrayDelimiter, Boolean.TYPE);
        }

        @Override
        public void extract(char[] data, int offset, int length) {
            int numberOfValues = numberOfValues(data, offset, length);
            value = numberOfValues > 0 ? new boolean[numberOfValues] : EMPTY;
            for (int arrayIndex = 0, charIndex = 0; arrayIndex < numberOfValues; arrayIndex++, charIndex++) {
                int numberOfChars = charsToNextDelimiter(data, offset + charIndex, length - charIndex);
                value[arrayIndex] = extractBoolean(data, offset + charIndex, numberOfChars);
                charIndex += numberOfChars;
            }
        }
    }
}
