data "aws_iam_policy_document" "library_workflow_assume_role" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type = "Service"
      identifiers = [
        "lambda.amazonaws.com",
        "states.amazonaws.com",
        "events.amazonaws.com"
      ]
    }
  }
}

resource "aws_iam_role" "library_workflow" {
  name               = "certforge-library-workflow"
  assume_role_policy = data.aws_iam_policy_document.library_workflow_assume_role.json
}

data "aws_iam_policy_document" "library_workflow_permissions" {
  statement {
    effect    = "Allow"
    actions   = ["lambda:InvokeFunction"]
    resources = [aws_lambda_function.library_workflow_task.arn]
  }

  statement {
    effect    = "Allow"
    actions   = ["states:StartExecution"]
    resources = [aws_sfn_state_machine.library_borrow.arn]
  }
}

resource "aws_iam_role_policy" "library_workflow_permissions" {
  name   = "certforge-library-workflow-permissions"
  role   = aws_iam_role.library_workflow.id
  policy = data.aws_iam_policy_document.library_workflow_permissions.json
}
