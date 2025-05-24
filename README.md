## APIGHOST 
![image](https://github.com/user-attachments/assets/eb01ebc1-810c-4562-922e-41b0c6a1c6fd)
<br>

## Table of Contents
1. Overview
2. QuickStart
3. Command List
4. API List
5. Relationship with other repositories in the organization
6. How to Contribute
7. License

<br><br>

## Overview
**API Ghost is a tool that allows you to easily create complex API integration tests and load tests with just drag and drop.** <br><br>
 You can connect APIs in sequence on a web screen without writing code, or define test scenarios with simple YAML files. <br>
You can test from local development to actual service environments in the same way, and check results with reports that you can see at a glance, greatly reducing test time for development and QA teams.


<br><br>

## QucickStart

### 1. **Install apighost with curl on the computer where you want to run tests** 

```bash
curl -fsSL https://github.com/api-ghost/api-ghost/releases/download/BETA-0.0.1/install.sh | sed 's/\r$//' | bash
```

<br>

### 2. **Config file setup**
 If you installed Apighost with curl, a new folder will be installed at `home\user\.apighost`. <br>
You need to create a configuration file named `api-ghost.yaml` inside that folder. <br><br> The configuration file should contain mainly <br>
- OpenAI service key 
- The URL of the service where your project you want to test is running. 
<br>

```yaml
apighost:
  tool:
    openAiKey: your-service-key
    libs:
      - https://yousinsa.duckdns.org/app1
      - https://yousinsa.duckdns.org/app2
      - https://yousinsa.duckdns.org/app3
```

<br>

### 3. **Writing scenarios** 
```bash
apighost edit filename.yaml
```

You can create scenario YAML files in the console window through commands. (Currently not supported in ubuntu environment. In that environment, please write scenarios through GUI.)  [GUI mode installation and usage guide](https://github.com/cod0216/api-ghost-ui) 

<br>

### 4. **Execute integration API test**
```bash
apighost execute filename.yaml
```

![scenariotest_capture](https://github.com/user-attachments/assets/1d520c48-fc0a-41d5-802f-992ddae70803)

You can run integration API tests based on scenarios in the console window through commands.

<br>

### 5. **Execute load test**
```bash
apighost loadtest filename.yaml
```
![apighost_cli_loadtest](https://github.com/user-attachments/assets/4e6c747f-0571-485c-aefd-85632f8917c8)

You can run load tests based on the scenarios you wrote and load test settings through commands.



<br><br>

## Command List

- `apighost ls`: View list of scenario test files (YAML)
    
- `apighost ls result`: View list of test results (JSON)
    
- `apighost cat filename.json`: View test result contents
    
- `apighost ui`: Enter GUI mode
  
- `apighsot execute filename.yaml` : Execute scenario test
  
- `apighost ls loadtest` :  View load test YAML list
  
- `apighost loadtest filename.yaml` : Execute load test
  
- `apighost rm filename.yaml` : Delete files (scenario, load test, result files)


<br><br>

## API List

### Scenario <br>

| Method | Endpoint                  | Description                                              |    
| ------ | ------------------------- | -------------------------------------------------------- |
| GET    | `/apighost/scenario-list` | Retrieve list of scenario test files                     | 
| GET    | `/apighost/scenario-info` | Retrieve detailed information of a scenario test         |  
| GET    | `/apighost/result-list`   | Retrieve list of scenario test result files              |     
| GET    | `/apighost/result-info`   | Retrieve detailed information of a scenario test result  |     
| GET    | `/apighost/endpoint-json` | Collect endpoint data                                    |     
| GET    | `/apighost/scenario-test` | Execute scenario test and return results every 5 seconds |     
| POST   | `/apighost/generate-data` | Generate LLM-based body and parameter values             |     

<br>

### Loadtest <br>


| Method | Endpoint                     | Description                                      |    
| ------ | ---------------------------- | ------------------------------------------------ |
| GET    | `/apighost/loadtest-list`    | Retrieve list of load test configuration files   | 
| POST   | `/apighost/loadtest-export`  | Create a load test configuration file            |  
| GET    | `/apighost/loadtest-info`    | Retrieve detailed info of a load test config     |  
| GET    | `/apighost/loadtest-execute` | Execute load test and receive real-time results  |  

<br><br>

## Module Description
APIGHOST has adopted a multi-module structure. <br>

`cli`: Implements commands to use apighost through terminal commands <br><br>
`core`: Main functions of ApiGhost such as load tests, scenario tests, etc. <br><br>
`generator`: Generate random parameter values using LLM  <br><br>
`loadtest`: Key functions related to load testing <br><br>
`model`:Define Java model formats to use in scenario tests and load tests <br><br>
`parser`: Conversion logic related to file input/output that has the same content but different formats <br><br>
`util`: Functions commonly used in multiple modules, such as time initialization or finding user directory locations  <br><br>
`web`: Web-related modules using vanilla servlets for interaction with GUI <br><br>


Some of the above modules are partially shared with [ApiGhost Agent](https://github.com/api-ghost/api-ghost-agent-spring).
<br><br>

## Contribution Guide

- Please submit issues for bug reports or feature suggestions.  
    [Bug Issue](https://github.com/api-ghost/api-ghost/issues)
    
- Follow the pull request and code review process.  
    [PR Issue](https://github.com/api-ghost/api-ghost/pulls)
    

<br><br>

##  License

This project is licensed under the **[MIT License](https://github.com/api-ghost/api-ghost/blob/main/LICENSE)**.





