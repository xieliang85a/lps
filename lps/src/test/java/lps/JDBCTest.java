package lps;

import cn.com.xl.core.toolbox.kit.AESKit;

public class JDBCTest {
public static void main(String[] args) {
	System.out.println(AESKit.encrypt("mysql"));
	System.out.println(AESKit.encrypt("com.mysql.jdbc.Driver"));
	System.out
			.println(AESKit
					.encrypt("jdbc:mysql://localhost:3306/lps?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true"));
	System.out.println(AESKit.encrypt("root"));
	System.out.println(AESKit.encrypt("Root_123"));
}
}
