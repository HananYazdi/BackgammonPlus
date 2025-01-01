package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Level {
	@JsonProperty("Easy")
	EASY,

	@JsonProperty("Medium")
	MEDIUM,

	@JsonProperty("Hard")
	HARD;

	@JsonCreator
	public static Level fromString(String value) {
		switch (value.toUpperCase()) {
		case "EASY":
			return EASY;
		case "MEDIUM":
			return MEDIUM;
		case "HARD":
			return HARD;
		default:
			throw new IllegalArgumentException("Unknown difficulty: " + value);
		}
	}
}
