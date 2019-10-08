

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class GetHostInfo {

	private static Logger logger = Logger.getLogger(GetHostInfo.class); // 日志记录

	@SuppressWarnings("resource")
	public static String GetHostCPUSerialNo() { // 获取主机CPU唯一编号
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(new String[] { "wmic", "cpu", "get", "ProcessorId" });
			process.getOutputStream().close();

			Scanner sc = new Scanner(process.getInputStream());
			// String property = sc.next();
			String serial = sc.next();
			// System.out.println(property + ": " + serial);
			return serial;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error("获取主机CPU失败�?", e);
			return "unknow";
		}
	}

	/**
	 * 获取本机的服务器的IP地址，非127.0.0.1
	 * 
	 * @return
	 */
	public static String GetHostIP() { // 获取网关主机ip
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress() // loopback地址即本机地�?，IPv4的loopback范围�?127.0.0.0
																							// ~
																							// 127.255.255.255
							&& ip.getHostAddress().indexOf(":") == -1) {
						return ip.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String cpuId = GetHostInfo.GetHostCPUSerialNo();
		String ip = GetHostInfo.GetHostIP();
		System.out.println(cpuId);
		System.out.println(ip);
	}

}
