package yujia.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HttpUtil {

	protected static final String tag = "HttpUtilis";

	/**
	 * 根据提供的url地址进行网络连接，并返回服务器返回的字符串
	 * 
	 * @param qString
	 * @return
	 * @throws Exception
	 */
	public static String getStringByGet(String qString) throws Exception {
		Logger.i("getStringByURLConnection called url = " + qString);
		InputStream in = getInputStreamByGet(qString);
		String str = getStringFromInputStream(in);
		return str;

	}

	public static String getStringByGet(String qString, String charsetName)
			throws Exception {
		Logger.i("getStringByGet called url = " + qString);
		InputStream in = getInputStreamByGet(qString);
		// InputStream in = getInputSreamByApacheGet(qString);
		String str = getStringFromInputStream(in, charsetName);
		return str;

	}

	public static String getStringByPost(String qString) throws Exception {
		InputStream in = getInputStreamByPost(qString);
		String str = getStringFromInputStream(in);
		return str;

	}

	@SuppressWarnings("finally")
	public static String getStringFromInputStream(InputStream in)
			throws Exception {
		Logger.i("getStringFromInputStream called inputstream = " + in);
		if (in == null)
			return null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				"utf8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		reader.close();
		in.close();
		return sb.toString();

	}

	@SuppressWarnings("finally")
	public static String getStringFromInputStream(InputStream in,
			String charsetName) throws Exception {
		Logger.i("getStringFromInputStream called inputstream = " + in);
		if (in == null)
			return null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				charsetName));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		reader.close();
		in.close();
		return sb.toString();

	}

	public static InputStream getInputStreamByGet(String queryString)
			throws MalformedURLException, IOException, ProtocolException {
		URL url;
		url = new URL(queryString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setRequestProperty("HOST", url.getHost());
		conn.setRequestMethod("GET");
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36");
		/*
		 * conn.setRequestProperty( "Accept-Encoding", "gzip,deflate,sdch");
		 */
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language",
				"en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4,ja;q=0.2,zh-TW;q=0.2,ko;q=0.2");
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		conn.setRequestProperty(
				"Cookie",
				"pnm_cku822=233fCJmZk4PGRVHHxtOZXsnZHo"
						+ "9ay11PWsQKg%3D%3D%7CfyJ6Zyd9N2ggYHUnZXMrahs%3D%7CfiB4D157YHtufDUqfHY4fmo7dCQaAxlbU1AFS2IT%7CeSRiYjNhIHA4emQ3cGM5fWUjejh%2FO3xrO31rMnZuKX85ZiNkcCF3DA%3D%3D%7CeCVoaEAQTh5fGRNHHxsOABxNc1w%3D%7CeyR8C08fQxlaHQhcHQNYGgJHEFoHWxoKQw8aRQIdXA9LE1QWB1UZE04LEFcYWQRADQdRFwVeBh1eCVcPTgMVXh0LU3oL%7CeiJmeiV2KHMvangudmM6eXk%2BAA%3D%3D; cna=ZA/mCIYT9AcCAdn3UmX4aMMp; cq=ccp%3D1; CNZZDATA1000279581=596835340-1398162954-http%253A%"
						+ "252F%252Fs.taobao.com%252F%7C1398683461");
	
		/*
		 * Map<String, List<String>> map = conn.getRequestProperties();
		 * CookieManager cookimgr = (CookieManager) CookieHandler.getDefault();
		 * List<URI> uris = cookimgr.getCookieStore().getURIs();
		 * List<HttpCookie> cookies = cookimgr.getCookieStore().getCookies();
		 * Logger.i("request header map = " + map.toString());
		 * Logger.i("cookie uris = " + uris.toString()); Logger.i("cookies = " +
		 * cookies.toString());
		 */
		conn.setReadTimeout(30 * 1000);
		int code = conn.getResponseCode();
		Logger.i("response code = " + code);
		Map<String, List<String>> map1 = conn.getHeaderFields();
		// Logger.i("response header map = " + map1.toString());
		if (code == 200) {
			Logger.i("消息成功发送至服务器");
			return conn.getInputStream();
		}
		return null;
	}
	
	public static HttpURLConnection getHttpGetConnection(String queryString)
			throws MalformedURLException, IOException, ProtocolException {
		URL url;
		url = new URL(queryString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setRequestProperty("HOST", url.getHost());
		conn.setRequestMethod("GET");
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36");
		/*
		 * conn.setRequestProperty( "Accept-Encoding", "gzip,deflate,sdch");
		 */
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language",
				"en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4,ja;q=0.2,zh-TW;q=0.2,ko;q=0.2");
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		conn.setRequestProperty(
				"Cookie",
				"pnm_cku822=233fCJmZk4PGRVHHxtOZXsnZHo"
						+ "9ay11PWsQKg%3D%3D%7CfyJ6Zyd9N2ggYHUnZXMrahs%3D%7CfiB4D157YHtufDUqfHY4fmo7dCQaAxlbU1AFS2IT%7CeSRiYjNhIHA4emQ3cGM5fWUjejh%2FO3xrO31rMnZuKX85ZiNkcCF3DA%3D%3D%7CeCVoaEAQTh5fGRNHHxsOABxNc1w%3D%7CeyR8C08fQxlaHQhcHQNYGgJHEFoHWxoKQw8aRQIdXA9LE1QWB1UZE04LEFcYWQRADQdRFwVeBh1eCVcPTgMVXh0LU3oL%7CeiJmeiV2KHMvangudmM6eXk%2BAA%3D%3D; cna=ZA/mCIYT9AcCAdn3UmX4aMMp; cq=ccp%3D1; CNZZDATA1000279581=596835340-1398162954-http%253A%"
						+ "252F%252Fs.taobao.com%252F%7C1398683461");
	
		conn.setReadTimeout(30 * 1000);
		return conn;
	}

	public static InputStream getInputStreamByPost(String queryString)
			throws MalformedURLException, IOException, ProtocolException {
		/*
		 * Loger.i("getInputStreamByPost called url= " + queryString); URL url;
		 * InputStream in; url = new URL(queryString); HttpURLConnection conn =
		 * (HttpURLConnection) url.openConnection();
		 * conn.setRequestMethod("POST"); conn.setReadTimeout(30 * 1000);
		 * conn.setRequestProperty(G.URL_SENDER_Lable,
		 * G.context.getPackageName()); int code = conn.getResponseCode();
		 * i("response code = " + code); if (code == 200) { i("消息成功发送至服务器");
		 * return conn.getInputStream(); }
		 */
		return null;
	}

	public static void loadUrlInWebView(final Context context, String url) {
		WebView view = new WebView(context);
		view.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				if (url.contains("loginOutSuss")) {

				} else if (url.contains("successful")) {
				} else {
				}
			}

		});

		view.loadUrl(url);

	}

	public static HttpURLConnection getConnectionByGet(String queryString)
			throws MalformedURLException, IOException, ProtocolException {
		URL url;
		url = new URL(queryString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setReadTimeout(30 * 1000);
		return conn;
	}

	public static Object getObjectByGet(String queryString)
			throws MalformedURLException, IOException, ProtocolException {
		URL url;
		url = new URL(queryString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setReadTimeout(30 * 1000);
		Object ob = conn.getContent();
		return ob;
	}

	public static InputStream getInputSreamByApacheGet(String url) {
		Logger.i("getInputSreamByApacheGet url= " + url);
		if (url == null)
			return null;
		DefaultHttpClient httpClient = null;
		InputStream in = null;
		try {
			// 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
			HttpParams params = new BasicHttpParams();

			// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
			HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
			HttpConnectionParams.setSoTimeout(params, 20 * 1000);
			HttpConnectionParams.setSocketBufferSize(params, 8192);

			// 设置重定向，缺省为true
			HttpClientParams.setRedirecting(params, true);

			// 设置user agent
			HttpProtocolParams
					.setUserAgent(
							params,
							"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

			// 创建一个 HttpClient 实例
			// 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient
			// 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现DefaultHttpClient
			httpClient = new DefaultHttpClient(params);
			httpClient.setRedirectHandler(new RedirectHandler() {

				@Override
				public boolean isRedirectRequested(
						HttpResponse paramHttpResponse,
						HttpContext paramHttpContext) {
					Header header = paramHttpResponse
							.getFirstHeader("Location");
					if (header != null) {
						String url = header.getValue();
						Logger.ptSilder();
						Logger.i("redicrect " + url);
						try {
							String result = getStringByGet(url, "gbk");
							Logger.i("redicrect " + result);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
					}
					return false;
				}

				@Override
				public URI getLocationURI(HttpResponse paramHttpResponse,
						HttpContext paramHttpContext)
						throws org.apache.http.ProtocolException {
					return null;
				}
			});
			// 创建 HttpGet 方法，该方法会自动处理 URL 地址的重定向
			HttpGet httpGet = new HttpGet(url);

			HttpResponse response = httpClient.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
			Logger.i("respose code= " + code);
			if (code != HttpStatus.SC_OK) {
				// 错误处理，例如可以在该请求正常结束前将其中断
				httpGet.abort();
			} else {
				in = response.getEntity().getContent();
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			// 释放连接
			httpClient.getConnectionManager().shutdown();
		}
		return in;

	}

	public static void connectChinaNetByPost(String url, HttpEntity entity) {
		if (url == null)
			return;
		DefaultHttpClient httpClient = null;
		try {
			// 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
			HttpParams params = new BasicHttpParams();

			// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
			HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
			HttpConnectionParams.setSoTimeout(params, 20 * 1000);
			HttpConnectionParams.setSocketBufferSize(params, 8192);

			// 设置重定向，缺省为true
			HttpClientParams.setRedirecting(params, true);

			// 设置user agent
			HttpProtocolParams.setUserAgent(params, null);

			// 创建一个 HttpClient 实例
			// 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient
			// 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现DefaultHttpClient
			httpClient = new DefaultHttpClient(params);
			httpClient.setRedirectHandler(new RedirectHandler() {

				@Override
				public boolean isRedirectRequested(
						HttpResponse paramHttpResponse,
						HttpContext paramHttpContext) {
					Header header = paramHttpResponse
							.getFirstHeader("Location");
					if (header != null) {
						String url = header.getValue();
						Logger.i("redirct url = " + url);
					}
					return false;
				}

				@Override
				public URI getLocationURI(HttpResponse paramHttpResponse,
						HttpContext paramHttpContext)
						throws org.apache.http.ProtocolException {
					return null;
				}
			});
			// 创建 HttpGet 方法，该方法会自动处理 URL 地址的重定向
			HttpPost post = new HttpPost(url);
			post.setEntity(entity);
			HttpResponse response = httpClient.execute(post);
			int code = response.getStatusLine().getStatusCode();
			if (code != HttpStatus.SC_OK) {
				// 错误处理，例如可以在该请求正常结束前将其中断
				post.abort();
			}

		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			// 释放连接
			httpClient.getConnectionManager().shutdown();
		}

	}

	@SuppressWarnings("finally")
	public static boolean isNetConnected() {
		DefaultHttpClient httpClient = null;
		boolean isConn = false;
		try {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
			HttpConnectionParams.setSoTimeout(params, 20 * 1000);
			HttpConnectionParams.setSocketBufferSize(params, 8192);

			HttpClientParams.setRedirecting(params, true);

			HttpProtocolParams.setUserAgent(params, null);
			httpClient = new DefaultHttpClient(params);
			HttpGet httpGet = new HttpGet("http://www.baidu.com");

			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				// 错误处理，例如可以在该请求正常结束前将其中断
				httpGet.abort();
			} else {
				isConn = true;
			}

		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
			return isConn;
		}

	}

	public static void loadUrlInWebView(Context context, String url,
			WebViewClient webViewClient) {
		WebView view = new WebView(context);
		view.setWebViewClient(webViewClient);
		view.loadUrl(url);
	}

	public static void doRandomConnect(String url) {

		if (url == null)
			return;
		DefaultHttpClient httpClient = null;
		try {
			// 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
			HttpParams params = new BasicHttpParams();

			// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
			HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
			HttpConnectionParams.setSoTimeout(params, 20 * 1000);
			HttpConnectionParams.setSocketBufferSize(params, 8192);

			// 设置重定向，缺省为true
			HttpClientParams.setRedirecting(params, true);

			// 设置user agent
			HttpProtocolParams.setUserAgent(params, null);
			httpClient = new DefaultHttpClient(params);
			HttpGet httpGet = new HttpGet(url);

			httpClient.setRedirectHandler(new RedirectHandler() {

				@Override
				public boolean isRedirectRequested(
						HttpResponse paramHttpResponse,
						HttpContext paramHttpContext) {
					Header header = paramHttpResponse
							.getFirstHeader("Location");
					if (header != null) {
						String url = header.getValue();
						Logger.i("redict url = " + url);
					}
					return false;
				}

				@Override
				public URI getLocationURI(HttpResponse paramHttpResponse,
						HttpContext paramHttpContext)
						throws org.apache.http.ProtocolException {
					return null;
				}
			});
			HttpResponse response = httpClient.execute(httpGet);
			Logger.i("doRandomConnect called code = "
					+ response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				// 错误处理，例如可以在该请求正常结束前将其中断
				httpGet.abort();
			}

		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			// 释放连接
			httpClient.getConnectionManager().shutdown();
		}

	}

	public static String getStrLineFromInputSream(InputStream in) {
		if (in == null)
			return null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String str = null;
		try {
			str = reader.readLine().trim();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				return str;
			}
		}
	}

	/**
	 * 对字符串进行url编码
	 * 
	 * @param str
	 * @return
	 */
	public static String getUrlEncodeStr(String str) {
		return URLEncoder.encode(str);
	}

	/**
	 * 获得当前网络连接的名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getActivitNetWorkInfo(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		if (info == null)
			return null;
		String type = info.getTypeName();
		return type;
	}

	/*
	 * public static InputStream sendMsgByPost(IMessage msg) throws IOException
	 * { pt("sendMsgByPost called"); logMsg(msg); URL url = new
	 * URL(Refs.MESSAGE_ACTION); HttpURLConnection conn = (HttpURLConnection)
	 * url.openConnection(); conn.setRequestMethod("POST");
	 * conn.setConnectTimeout(30 * 1000);
	 * conn.setRequestProperty(Refs.URL_SENDER_Lable, Refs.URL_SENDER);
	 * conn.setRequestProperty("format", "json");
	 * conn.setRequestProperty("Content-Type",
	 * "application/json;charset=UTF-8"); conn.setDoOutput(true); OutputStream
	 * out = conn.getOutputStream(); JsonWriter writer = new JsonWriter(new
	 * OutputStreamWriter(out, "UTF-8")); writer.beginObject();
	 * msg.wirteMsgByJson(writer); writer.endObject(); writer.close(); int code
	 * = conn.getResponseCode(); pt("response code = " + code); if (code == 200)
	 * { pt("消息成功发送至服务器"); return conn.getInputStream(); } return null; }
	 */

	public static InputStream sendMsgByHttpGet(String urlstr) throws Exception {
		/*
		 * i("sendMsgByGet called"); URL url = new URL(urlstr);
		 * HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		 * conn.setConnectTimeout(10 * 1000); conn.setRequestMethod("GET");
		 * conn.setRequestProperty(G.URL_SENDER_Lable,
		 * G.context.getPackageName()); int code = conn.getResponseCode();
		 * i("response code = " + code); if (code == 200) { i("消息成功发送至服务器");
		 * return conn.getInputStream(); }
		 */
		return null;
	}

	/**
	 * 根据URL地址返回字节数据
	 * 
	 * @param imagePath
	 * @return
	 * @throws IOException
	 * @throws ProtocolException
	 * @throws MalformedURLException
	 */
	public static byte[] getByteArrayByUrl(String url) throws Exception {
		Logger.i("getByteArrayByUrl called  url= " + url);
		InputStream in = getInputStreamByGet(url);
		if (in == null)
			return null;
		BufferedInputStream bin = new BufferedInputStream(in);
		byte[] buffer = new byte[1024 * 8];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		while ((len = bin.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		bin.close();
		out.close();
		return out.toByteArray();

	}

	public static InputStream getInputStreamByHttpConnection(HttpURLConnection conn) throws Exception {
		int code = conn.getResponseCode();
		Logger.i("response code = " + code);
		Map<String, List<String>> map1 = conn.getHeaderFields();
		// Logger.i("response header map = " + map1.toString());
		if (code == 200) {
			return conn.getInputStream();
		}
		return null;
	}

}
