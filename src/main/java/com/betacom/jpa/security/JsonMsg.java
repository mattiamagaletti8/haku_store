package com.betacom.jpa.security;

/**
 * Serializza a mano il corpo {"msg": "..."} (stessa forma di ResponseDTO) per i punti
 * della security filter chain che rispondono prima che il container Spring MVC/Jackson entri in gioco.
 */
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
