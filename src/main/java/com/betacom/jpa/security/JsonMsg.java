package com.betacom.jpa.security;

// Proprietario: Sara e Mattia
final class JsonMsg {

	private JsonMsg() {
	}

	static String responseDTOJson(String msg) {
		return "{\"msg\":\"" + escape(msg) + "\"}";
	}

	private static String escape(String s) {
		if (s == null)
			return "";
		return s.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}
