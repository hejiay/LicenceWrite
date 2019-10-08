

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpClientUtil {
	private static Logger logger = Logger.getLogger(HttpClientUtil.class); // 日志记录

	/**
	 * 
	 * @param url
	 *            路径
	 * @param param
	 *            参数列表
	 * @return
	 */
	public static String doGet(String url, Map<String, String> param) {
		// 创建HttpClient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();
			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);
			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状�?�是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			} else {
				logger.error("Get请求提交失败�?" + url);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("Get请求提交失败�?" + url, e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
				logger.info("关闭�?有链接！");
			} catch (IOException e) {
				// e.printStackTrace();
				logger.error("关闭�?有链接失败！", e);
			}
		}
		return resultString;
	}

	public static String doGet(String url) {
		return doGet(url, null);
	}

	/**
	 * 
	 * @param url
	 *            路径
	 * @param param
	 *            参数列表
	 * @return
	 */
	public static String doPost(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// // 解决中文乱码问题
			// StringEntity entity = new StringEntity(param.toString(),
			// "utf-8");
			// entity.setContentEncoding("UTF-8");
			// entity.setContentType("text/html");
			// httpPost.setEntity(entity);
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("Post请求提交失败�?" + url, e);
		} finally {
			try {
				response.close();
				logger.info("关闭�?有链接！");
			} catch (IOException e) {
				// e.printStackTrace();
				logger.error("关闭�?有链接失败！", e);
			}
		}
		return resultString;
	}

	public static String doPost(String url) {
		return doPost(url, null);
	}

	/**
	 * 发�?�Json数据
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	public static String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("PostJson请求提交失败�?" + url, e);
		} finally {
			try {
				response.close();
				logger.info("关闭�?有链接！");
			} catch (IOException e) {
				// e.printStackTrace();
				logger.error("关闭�?有链接失败！", e);
			}
		}
		return resultString;
	}

	// 上传文件功能模块
	/**
	 * 发�?�post请求
	 * 
	 * @param requestUrl
	 *            请求url
	 * @param requestHeader
	 *            请求�?
	 * @param formTexts
	 *            表单数据
	 * @param files
	 *            上传文件
	 * @param requestEncoding
	 *            请求编码
	 * @param responseEncoding
	 *            响应编码
	 * @return 页面响应html
	 */
	public static String sendPost(String requestUrl, Map<String, String> requestHeader, Map<String, String> formTexts,
			Map<String, String> files, String requestEncoding, String responseEncoding) {
		OutputStream out = null;
		BufferedReader reader = null;
		String result = "";
		try {
			if (requestUrl == null || requestUrl.isEmpty()) {
				return result;
			}
			URL realUrl = new URL(requestUrl);
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestProperty("accept", "text/html, application/xhtml+xml, image/jxr, */*");
			connection.setRequestProperty("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
			if (requestHeader != null && requestHeader.size() > 0) {
				for (Entry<String, String> entry : requestHeader.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			if (requestEncoding == null || requestEncoding.isEmpty()) {
				requestEncoding = "UTF-8";
			}
			if (responseEncoding == null || responseEncoding.isEmpty()) {
				responseEncoding = "UTF-8";
			}
			if (requestHeader != null && requestHeader.size() > 0) {
				for (Entry<String, String> entry : requestHeader.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			if (files == null || files.size() == 0) {
				connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
				out = new DataOutputStream(connection.getOutputStream());
				if (formTexts != null && formTexts.size() > 0) {
					String formData = "";
					for (Entry<String, String> entry : formTexts.entrySet()) {
						formData += entry.getKey() + "=" + entry.getValue() + "&";
					}
					formData = formData.substring(0, formData.length() - 1);
					out.write(formData.toString().getBytes(requestEncoding));
				}
			} else {
				String boundary = "-----------------------------" + String.valueOf(new Date().getTime());
				connection.setRequestProperty("content-type", "multipart/form-data; boundary=" + boundary);
				out = new DataOutputStream(connection.getOutputStream());
				if (formTexts != null && formTexts.size() > 0) {
					StringBuilder sbFormData = new StringBuilder();
					for (Entry<String, String> entry : formTexts.entrySet()) {
						sbFormData.append("--" + boundary + "\r\n");
						sbFormData.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
						sbFormData.append(entry.getValue() + "\r\n");
					}
					out.write(sbFormData.toString().getBytes(requestEncoding));
				}
				for (Entry<String, String> entry : files.entrySet()) {
					String fileName = entry.getKey();
					String filePath = entry.getValue();
					if (fileName == null || fileName.isEmpty() || filePath == null || filePath.isEmpty()) {
						continue;
					}
					File file = new File(filePath);
					if (!file.exists()) {
						continue;
					}
					out.write(("--" + boundary + "\r\n").getBytes(requestEncoding));
					out.write(("Content-Disposition: form-data; name=\"" + fileName + "\"; filename=\"" + file.getName()
							+ "\"\r\n").getBytes(requestEncoding));
					out.write(("Content-Type: application/x-msdownload\r\n\r\n").getBytes(requestEncoding));
					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
					out.write(("\r\n").getBytes(requestEncoding));
				}
				out.write(("--" + boundary + "--\r\n").getBytes(requestEncoding));
			}
			out.flush();
			out.close();
			out = null;
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), responseEncoding));
			String line;
			while ((line = reader.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			// System.out.println("发�?�POST请求出现异常�?");
			// e.printStackTrace();
			logger.error("Post请求出现异常�?" + requestUrl, e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (reader != null) {
					reader.close();
				}
				logger.info("关闭�?有链接！");
			} catch (IOException ex) {
				// ex.printStackTrace();
				logger.error("关闭�?有链接异常！", ex);
			}
		}
		return result;
	}

	public static String HttpPost(String url, Map<String, String> param) {
		return doPost(url, param);
	}

	public static String APIHttpGet(String url, Map<String, String> param) {
		return doGet(url, param);
	}

	// // 使用示例
	// // 在服务器端只�?要使用request.getParameter("username")，即可获�?
	// public static void httpClientTest() {
	// Map<String, String> param = new HashMap<String, String>();
	// param.put("username", "admin");
	// String url = "http://127.0.0.1:8080";
	// String str = HttpClientUtil.doGet(url, param);
	// }

	public static String HttpUploadFile(String url, Map<String, String> formTexts, Map<String, String> files) {
		return sendPost(url, null, formTexts, files, null, null);
	}

	public static String HttpUploadXPSFile(String url, Map<String, String> formTexts, Map<String, String> files) {
		return sendPost(url, null, formTexts, files, null, null);
	}

	public static String HttpUploadIMGFile(String url, Map<String, String> formTexts, Map<String, String> files) {
		return sendPost(url, null, formTexts, files, null, null);
	}

	/**
	 * 从网络url中下载文�?
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为3�?
		conn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返�?403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 得到输入�?
		InputStream inputStream = conn.getInputStream();
		// 获取自己数组
		byte[] getData = readInputStream(inputStream);

		// 文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}

		System.out.println("info:" + url + " download success");

	}

	/**
	 * 从输入流中获取字符数�?
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	/**
	 * 调用downLoadFromUrl进行下载文件
	 * 
	 * @param urlStr
	 *            下载文件的url地址
	 * @param fileName
	 *            下载文件的文件名
	 * @param savePath
	 *            下载文件的本地存储地�?
	 * @return
	 */
	public boolean DownLoadFile(String urlStr, String fileName, String savePath) {
		try {
			downLoadFromUrl(urlStr, fileName, savePath);
			logger.info("文件下载成功�?" + urlStr);
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
			logger.error("文件下载失败�?" + urlStr, e);
			return false;
		}
	}

}
