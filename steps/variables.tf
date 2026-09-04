variable "aws_region" {
  description = "AWS region used by the LocalStack emulation."
  type        = string
  default     = "us-east-1"
}

variable "localstack_endpoint" {
  description = "Base endpoint exposed by the local LocalStack instance."
  type        = string
  default     = "http://localhost:4566"
}

variable "aws_access_key" {
  description = "LocalStack access key; this is not a real AWS credential."
  type        = string
  default     = "test"
}

variable "aws_secret_key" {
  description = "LocalStack secret key; this is not a real AWS credential."
  type        = string
  default     = "test"
  sensitive   = true
}
