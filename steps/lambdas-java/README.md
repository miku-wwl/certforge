# Java Step Functions demo Lambda

This module intentionally keeps Lambda tiny. Step Functions is the subject of the demo, so one Lambda handler supports three task names instead of implementing a full library domain.

Handler:

```text
com.certforge.library.lambda.LibraryWorkflowTaskHandler
```

Supported task inputs:

```text
validate -> 借阅请求已通过校验
record   -> 借阅记录已创建
notify   -> 借阅结果已通知读者
```

Step Functions will call the same handler three times. The order and transitions belong in the state machine definition.

The handler is plain Java and does not start Spring Boot or call AWS services. DynamoDB and SNS resources can be added later when the workflow shape is understood.

Step Functions passes a small task input to the handler:

```json
{
  "step": "validate",
  "requestId": "req-001"
}
```

Build the Lambda JAR:

```powershell
mvn package
```
