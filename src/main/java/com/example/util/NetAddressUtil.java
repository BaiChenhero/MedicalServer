package com.example.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetAddressUtil {

	public static String getCurrentServerIPAddress() {
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}
}
