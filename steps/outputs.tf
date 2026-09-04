output "library_lambda_function_name" {
  description = "The Lambda function used by all three workflow tasks."
  value       = aws_lambda_function.library_workflow_task.function_name
}

output "library_state_machine_arn" {
  description = "The three-step library borrowing state machine ARN."
  value       = aws_sfn_state_machine.library_borrow.arn
}

output "library_event_rule_name" {
  description = "The EventBridge rule that starts the state machine."
  value       = aws_cloudwatch_event_rule.library_borrow_requested.name
}
