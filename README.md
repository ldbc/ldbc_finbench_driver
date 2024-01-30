![LDBC Logo](ldbc-logo.png)

# LDBC FinBench Driver

![Build status](https://github.com/ldbc/ldbc_finbench_driver/actions/workflows/ci.yml/badge.svg?branch=main)

The LDBC FinBench Driver is a powerful tool designed for benchmarking the performance of graph databases. This is the alpha version of the FinBench driver, currently undergoing alpha testing.

## 1. Configurations

The Driver initiates by reading the configuration file. The default configuration file path for DummyDB is `src/main/resources/example/ldbc_finbench_driver_dummy.properties`.

### 1.1 SUT Defined

If required, the System Under Test (SUT) can customize the configuration parameters to connect to the database or read the query file. This can include the request host, port, path, etc.

```shell
host=localhost
port=9091
user=admin
pass=123456
path=cypher/
```

### 1.2 DB

For a new implementation of the SUT, update this configuration parameter accordingly.

```shell
db=org.ldbcouncil.finbench.impls.dummy.DummyDb
```

### 1.3 Params

Please update based on your parameter path. The `parameters_dir` is the `ComplexRead` parameter path, and `updates_dir` is the `Write` and `ReadWrite` incremental data path.

```shell
ldbc.finbench.transaction.queries.parameters_dir=src/main/resources/example/data/read_params
ldbc.finbench.transaction.queries.updates_dir=src/main/resources/example/data/incremental_data
```

### 1.4 Mode

The driver operates in three modes:

- CREATE_VALIDATION
- VALIDATE_DATABASE
- EXECUTE_BENCHMARK
- AUTOMATIC_TEST

#### CREATE_VALIDATION

With `CREATE_VALIDATION` mode, you create a database result. `validation_parameters_size` denotes the number of results created, while `validate_database` refers to the file where the created results are stored.

```shell
mode=CREATE_VALIDATION
validation_parameters_size=100
validate_database=validation_params.csv
```

#### VALIDATE_DATABASE

`VALIDATE_DATABASE` mode allows you to verify the SUT. The `validate_database` is the result created by `CREATE_VALIDATION` mode.

```shell
mode=VALIDATE_DATABASE
validate_database=validation_params.csv
```

#### EXECUTE_BENCHMARK

Perform the performance test with `EXECUTE_BENCHMARK` mode.  Here are some crucial configuration parameters that need adjustment when operating the driver:

1. **thread_count**: Represents the number of concurrent requests that the driver can handle, corresponding to the number of active threads running simultaneously within the driver.
2. **time_compression_ratio**: Controls the intensity of the driver's workload. A lower value yields a higher workload in a shorter timeframe.
3. **ignore_scheduled_start_times**: Determines whether the driver should follow the scheduled timings for sending requests. If set to true, the driver sends requests as soon as they are prepared, regardless of the schedule.
4. **warmup**: Denotes the number of preliminary test items processed before the actual benchmarking begins.
5. **operation_count**: Sets the number of test items executed during the actual benchmarking phase after the warm-up. 

```shell
mode=EXECUTE_BENCHMARK
thread_count=1
time_compression_ratio=0.001
ignore_scheduled_start_times=false
warmup=5
operation_count=10000
```


#### AUTOMATIC_TEST

Perform the performance test automatically with the `AUTOMATIC_TEST` mode, utilizing an adaptive testing approach. This approach leverages pre-execution on hardware devices for initial parameter estimation and achieves optimal performance, meeting requirements through automatic tuning. The entire process is divided into two phases: **(1) Rapid Estimation Phase** and **(2) Precision Tuning Phase**.

1. **estimate**: Quickly calculate the duration of each test phase (in milliseconds). If set to -1, it indicates the completion of operations to achieve the specified `warmup` value. If the parameter is not used, the default is 300,000 ms.
2. **accurate**: Specifies the duration of each test in the precise tuning phase (in milliseconds). If set to -1, it indicates the completion of operations to reach the specified `operation_count`. If the parameter is not used, the default is 7,200,000 ms.
3. **error_range**: Binary end condition, indicating the tolerance range. If the parameter is not used, the default is 1E-5.
4. **tcr_min**: Minimum time compression ratio limit. If the parameter is not used, the default is 1E-9.
5. **tcr_max**: Maximum time compression ratio limit. If the parameter is not used, the default is 1.
6. **timeout_rate**: Specifies the ratio of timeouts to total operations at which the threshold is allowed to be exceeded. If the parameter is not used, the default is 0.05.
7. **time_compression_ratio**: When utilized in the initial testing round, setting an appropriate value for the machine can significantly reduce the overall test duration. If the parameter is not used, the default is the average of (tcr_min + tcr_max) / 2.

```shell
mode=AUTOMATIC_TEST
estimate=300000
accurate=7200000
error_range=1E-5
tcr_min=1E-9
tcr_max=1
timeout_rate=0.05
time_compression_ratio=0.1
```

## 2. Quick Start

To get started, clone the repository and build it with Maven:

```bash
git clone https://github.com/ldbc/ldbc_finbench_driver.git
cd ldbc_finbench_driver
mvn clean package -DskipTests
```

For a quick trial of the driver, utilize the DummyDB shipped with it by running the following command:

```bash
java -cp target/driver-0.2.0-alpha.jar org.ldbcouncil.finbench.driver.driver.Driver -P src/main/resources/example/ldbc_finbench_driver_dummy.properties
```
To automatically find the suitable time_compression_ratio for your system on this machine, execute the following command:
```bash
java -cp target/driver-0.2.0-alpha.jar org.ldbcouncil.finbench.driver.driver.Driver -P src/main/resources/example/ldbc_finbench_automatic_test_dummy.properties
```

## 3. Reference

- FinBench Specification: https://github.com/ldbc/ldbc_finbench_docs
- FinBench DataGen: https://github.com/ldbc/ldbc_finbench_datagen
- FinBench Driver: https://github.com/ldbc/ldbc_finbench_driver
- FinBench Transaction Reference Implementation: https://github.com/ldbc/ldbc_finbench_transaction_impls 

Please visit these links for further documentation and related resources.
