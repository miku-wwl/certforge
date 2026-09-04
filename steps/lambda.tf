locals {
  library_lambda_jar = "${path.module}/lambdas-java/target/library-borrow-lambdas-1.0.0-SNAPSHOT.jar"
}

resource "aws_lambda_function" "library_workflow_task" {
  function_name = "certforge-library-workflow-task"
  role          = aws_iam_role.library_workflow.arn

  runtime = "java21"
  handler = "com.certforge.library.lambda.LibraryWorkflowTaskHandler"

  filename         = local.library_lambda_jar
  source_code_hash = filebase64sha256(local.library_lambda_jar)

  timeout     = 10
  memory_size = 512
}
