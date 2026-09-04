locals {
  library_borrow_definition = jsonencode({
    Comment = "Three-step library borrowing workflow"
    StartAt = "ValidateBorrow"
    States = {
      ValidateBorrow = {
        Type     = "Task"
        Resource = "arn:aws:states:::lambda:invoke"
        Parameters = {
          FunctionName = aws_lambda_function.library_workflow_task.function_name
          Payload = {
            step          = "validate"
            "requestId.$" = "$.requestId"
          }
        }
        ResultPath = "$.validation"
        Next       = "RecordBorrow"
      }

      RecordBorrow = {
        Type     = "Task"
        Resource = "arn:aws:states:::lambda:invoke"
        Parameters = {
          FunctionName = aws_lambda_function.library_workflow_task.function_name
          Payload = {
            step          = "record"
            "requestId.$" = "$.requestId"
          }
        }
        ResultPath = "$.record"
        Next       = "NotifyReader"
      }

      NotifyReader = {
        Type     = "Task"
        Resource = "arn:aws:states:::lambda:invoke"
        Parameters = {
          FunctionName = aws_lambda_function.library_workflow_task.function_name
          Payload = {
            step          = "notify"
            "requestId.$" = "$.requestId"
          }
        }
        ResultPath = "$.notification"
        End        = true
      }
    }
  })
}

resource "aws_sfn_state_machine" "library_borrow" {
  name       = "certforge-library-borrow"
  role_arn   = aws_iam_role.library_workflow.arn
  definition = local.library_borrow_definition
  type       = "STANDARD"
}

resource "aws_cloudwatch_event_rule" "library_borrow_requested" {
  name = "certforge-library-borrow-requested"

  event_pattern = jsonencode({
    source        = ["certforge.library"]
    "detail-type" = ["LibraryBorrowRequested"]
  })
}

resource "aws_cloudwatch_event_target" "start_library_borrow" {
  rule      = aws_cloudwatch_event_rule.library_borrow_requested.name
  arn       = aws_sfn_state_machine.library_borrow.arn
  role_arn  = aws_iam_role.library_workflow.arn
  target_id = "start-library-borrow"

  input_transformer {
    input_paths = {
      event_request_id = "$.detail.requestId"
    }

    input_template = <<-EOF
      {"requestId": <event_request_id>}
    EOF
  }
}
