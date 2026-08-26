package com.certforge.parser;

public record ParseFailure(int questionNumber, String reason, String sourceFragment) {
}
