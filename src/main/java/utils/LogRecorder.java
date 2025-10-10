package utils;

import okhttp3.Response;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;


public class LogRecorder {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    // 默认日志文件路径
    private static final String DEFAULT_LOG_FILE = "http_requests.log";

    /**
     * 记录 HTTP 响应信息到日志文件
     */
    public static void logResponse(Response response, String url) {
        logResponse(response, url, DEFAULT_LOG_FILE);
    }

    /**
     * 记录 HTTP 响应信息到指定日志文件
     */
    public static void logResponse(Response response, String url, String logFilePath) {
        lock.lock();
        try (FileWriter writer = new FileWriter(logFilePath, true);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bw)) {

            String timestamp = DATE_FORMAT.format(new Date());
            var statusCode = response.code();
            var headers = response.headers();
            var body = response.body();

            // 构建日志内容
            StringBuilder logEntry = new StringBuilder();
            logEntry.append("{");
            logEntry.append("\"Time\": ").append("\""+timestamp+"\"").append(",");
            logEntry.append("\"URL\": ").append("\""+url+"\"").append(",");
            logEntry.append("\"Status Code\": ").append(statusCode).append(",");
            logEntry.append("\"Response Headers\":" +
                    "[");

            // 添加所有响应头
            for (String headerName : headers.names()) {
                logEntry.append(headerName).append(": ")
                        .append("\""+headers.get(headerName)+"\"");
            }

            logEntry.append("}");

            // 写入文件
            out.println(logEntry);
            out.flush();

        } catch (IOException e) {
            System.err.println("写入日志文件失败: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * 记录失败的请求

    public static void logError(String url, String errorMessage) {
        logError(url, errorMessage, DEFAULT_LOG_FILE);
    }

    public static void logError(String url, String errorMessage, String logFilePath) {
        lock.lock();
        try (FileWriter writer = new FileWriter(logFilePath, true);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bw)) {

            String timestamp = DATE_FORMAT.format(new Date());

            String logEntry = "[" + timestamp + "] - ERROR\n" +
                    "URL: " + url + "\n" +
                    "Error: " + errorMessage + "\n" +
                    "-----------------------------\n";

            out.println(logEntry);
            out.flush();

        } catch (IOException e) {
            System.err.println("写入日志文件失败: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * 清空日志文件
     */
    public static void clearLog() {
        clearLog(DEFAULT_LOG_FILE);
    }

    public static void clearLog(String logFilePath) {
        lock.lock();
        try (FileWriter out = new FileWriter(logFilePath,false)) {
            // 清空文件内容
        } catch (IOException e) {
            System.err.println("清空日志文件失败: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
