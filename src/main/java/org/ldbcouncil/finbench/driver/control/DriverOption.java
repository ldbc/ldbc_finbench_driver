package org.ldbcouncil.finbench.driver.control;

public class DriverOption {
    String argName;
    String shortOption;
    String longOption;
    String description;
    Object defaultOption;


    public DriverOption(String argName, String shortOpt, String longOpt, String desc, Object defaultOption) {
        this.argName = argName;
        this.shortOption = shortOpt;
        this.longOption = longOpt;
        this.description = desc;
        this.defaultOption = defaultOption;
    }

    public String argName() {
        return argName;
    }

    public String shortOpt() {
        return shortOption;
    }

    public String longOpt() {
        return longOption;
    }

    public String desc() {
        return description;
    }

    public Object defaultOption() {
        return defaultOption;
    }

    public String defaultOptionToString() {
        return String.valueOf(defaultOption);
    }
}
