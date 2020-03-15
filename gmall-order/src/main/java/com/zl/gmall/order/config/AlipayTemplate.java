package com.zl.gmall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.zl.gmall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private   String app_id = "2016101600702073";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private  String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC8fdj3CMgA0tHD01bGBsTmG2IaBhLhWYCjaTVlAHjySLYB6wv5UQl4EyI4eNPhBsP0pSndDdoQQZ9RpcJl3s5ojgsYH8nd+Cdbs/MPku3+f858zd/f2ntjGmpA23unjS65RAfdPbWqW7PBi5oxPZkzfXWgsfzIKAvFuFzziRfryMB+3qrqf4VVygcG9pKrnmc1rZMyCmdvpsRyY8sn+yBIjwi5BnOr35dVW1wtXMJPh9OkGw4RilIPBmkIDOJQo/qXcrMReNBWe+zSxxSOCpP1kBtRclYfHhdmlHpoIMStcAH+mksfsQqAcyIsiJruuYlxMO86tVEFOQMGs/9LB6UZAgMBAAECggEAddZLLGzt9C3X+4+EwPn8YEqgMtq3NPBqTj54pfL8qaMxPdO+wZeaztlPXHSVKSm8mZYjrNF7EQylWDrDZEI9UfnzD5rX7Y+0s0y1WHgnlyI5aaafHegNmTArWu6byGyn2CSisfphNuZrY0UuGTiELT9yqQp8mHkImwwd+BYtwTXF7fvRq3m70NHaQJEUG+VATNkw6z20ShoIn6hpme8htGVu+acW6yFglnOpxqg7O56HMspujD1gL4gLC+f4s46uUFYaMU0UZiHC6tbkpoI8ykiYFnfzUjJNagMfg6s43HPLJMTepz9apKJKyNDwUTmaqKPW10kdrrE3ygJDQTJNvQKBgQDivWJEMoMUO3f9IuwCS5uew8HdSejIIJSUllzNsEWoWhUmXCVSNS4zYoqHVY4f6tiY+ZWVX7sGCMTz9KNX0ipt42zttH9rOZWqZFjij3hIe28ZxSpVLjbLKs6lqQmDJpxnZjf+PIaRNFetY6mWMjG1xyRQZybuaOYz+YlhTj8CTwKBgQDU0OO+kXmk1EdighP4P0+npZ3FPMk0iewGH2PmqlJqhPQeKjroxwnLHzItyS4HNf4FinaqxUrhP1hdxrH8ipaBUZPfjCDq4CotruVEOXmf5RdwAvyU3OYTVy5Jhcud4P+OnrULUq+YUf5a6nnzeKfRppjcalJ9LgDz6Ktxgk6QFwKBgQC5o2bGATkyonHd7e6e/no+JXYfEHknCQ29DdKTkUOmz4JLctwXj3TxGzfiD9kaJrQJoyamMsez7BAc0Gm/6FFWwQIZKFO+XVo9NIVVkTNg3LacucGsFfdEphqkpV4lY8JgId+sJq/iSKGUU2mwkA4FDzObJKlKPtZjLWYeJesOnwKBgQC/RuDrr6U92O4wQ+ne/kiBzgF3/aJ6blyNRDluYTeiNc2ULzCzd/H9dxSKwn90S7r9D+6InrsmqOCrqKLg8U9E/CWEj1YmxUQju0alg2Mlq6rdKlILGmBQkdMwkejpBgmsDkl6296ncUrEPGtPwepULQDHW2i4ylhg4Yb2hsBGEQKBgE3GftIi2mX6BfHrr6Jk6ljOMe2ZDUBrj/yQDn+NanQwieNmtIaWw7oyRIPs76rdX3/BVlzSh6BLiu0OtxTayV4iqmMaOzW3rojPvw8BMRGDJw5zuWrfQ+02qaJGZsdzprYfN1fPabT6V2hny4dK5aEmvN191n6lHDzSyFppuG+b";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private  String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkv9Dbcud9Tpukn2x6aJzhOnk1v5BV/FHtou93CXB+Esn3e1T1xmOWQMVPnhxCAuIqosuSXNZeAJEPtBifbTjhjlo1mvvIH0X/YE2kvbFos1oXYRo+0FH2mZUP8C6MSMHQ2SOIZyhBrkjkZ4tspBbxtWpncjadmaISWyY5YwtWdVuf4OZpPfri4eXB5qGrbebZct4IQ28XuRfgL9wEPenjwP8oasa3eFI/qlnvY1UvwUYTwJKIsasAPUS4nroKsCVD6EanP8c1DEtX8UywLGFw08K0kLOKQAK2YVDwytKG6FVsvWQu4QXHJKOaBXOaV+udyme1EcDiO9lZO+ehROpKwIDAQAB";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private  String notify_url;

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private  String return_url;

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
