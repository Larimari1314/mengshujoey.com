package com.mengshujoey.mengshuadminsystem.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * application name:mengshujoeyPatient - SendMessageByEmail
 * application describing:
 * copyright:
 * company:
 * time:2023-02-07 13:51:53
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@Component
public class SendMessageByEmail {
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${front-end-prefix}")
    private String prefix;
    @Async
    public void emailSending(Exception e) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        System.out.println("当前时间" + new Date());
        mailMessage.setSubject("服务器内部错误消息！请立即处理错误");
        mailMessage.setText("错误发生时间为" + new Date() + "，错误响应码为" + HttpStatus.SERVICE_UNAVAILABLE + "\r\n当前错误消息为:" + e.getMessage() + "\r\n请立即检查程序，防止服务器宕机。"
                + "\r\n此类消息由错误处理类自动处理如需要管理，请自行处理"
        );
        mailMessage.setTo("2163615907@qq.com");
        mailMessage.setFrom("sale.iove@qq.com");
        mailSender.send(mailMessage);
    }
    @Async
    public void resetPasswordSendMessage(String userName, String userAvatar, String userEmail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = null;
        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setSubject("萌叔joey管理端提醒您");
            messageHelper.setText("<meta charset='utf-8'><meta http-equiv='X-UA-Compatible' content='IE=edge'><meta name='viewport' content='width=device-width, initial-scale=1, shrink-to-fit=no'><meta name='description' content='Password Reset - Ac89 - Email Templates for developers'><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'><title>萌叔josy管理端密码更新提醒</title><style>html,body {margin: 0 auto !important;padding: 0 !important;width: 100% !important;font-family: sans-serif;line-height: 1.4;-webkit-font-smoothing: antialiased;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%; }* {-ms-text-size-adjust: 100%;}table,td {mso-table-lspace: 0pt !important;mso-table-rspace: 0pt !important;}img {display: block; border: none; max-width: 100%;-ms-interpolation-mode: bicubic;}a {text-decoration: none;}</style><table role='presentation' align='center' valign='top' border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#F3F5F9' style='border-spacing: 0; border-collapse: collapse; vertical-align: top; padding: 0; margin: 0; width: 100%; background: #F3F5F9'><tbody><tr><td height='30' style='height: 30px;'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr><tr><td align='center' valign='top'><table role='presentation' align='center' border='0' cellpadding='0' cellspacing='0' width='600' style='max-width: 600px; border-spacing: 0; border-collapse: collapse; vertical-align: top; padding: 0; margin: 0; width: 100%'><tbody><tr><td><p style='text-align: center; font-family: Arial, Helvetica, sans-serif; font-size: 12px; color: #99A1B3; margin: 0; padding: 0; line-height: 14px; font-weight: bold;padding: 0 20px;'>为确保送达，请添加<a href='mailto:no-reply@company.com' style='color: #0069FF'>sale.iove@qq.com</a>到您的好友列表</p></td></tr></tbody></table></td></tr><tr><td height='20&quot;' style='height: 20px;'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr><tr><td align='center' valign='top'><table role='presentation' align='center' border='0' cellpadding='0' cellspacing='0' width='600' bgcolor='#ffffff' style='max-width: 600px; border-spacing: 0; border-collapse: collapse; vertical-align: top; padding: 0; margin: 0; width: 100%;background: #ffffff;'><tbody><tr><td height='5' style='height: 5px; background-color: #0069FF; background-image: linear-gradient(-45deg, #0248FF, #0069FF);'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr><tr><td height='40&quot;' style='height:40px'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr><tr><td align='center' valign='top'><table role='presentation' align='center' border='0' cellpadding='0' cellspacing='0' width='440' style='max-width: 440px;border-spacing: 0; border-collapse: collapse; vertical-align: top; padding: 0; margin: 0;width: 100%;'><tbody><tr><td align='center'>" +
                    "<img src='" + userAvatar + "' width='170' style='border: none; display: block; max-width: 170px; width: 100%'></td></tr><tr><td height='40' style='height:40px'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr><tr><td><h1 style='font-family: Arial, Helvetica, sans-serif; font-size: 24px; color: #010E28; font-weight: bold; margin: 0; margin-bottom: 5px;padding: 0'>" +
                    "您好，" + userName + "!</h1><p style='font-family: Arial, Helvetica, sans-serif; font-size: 16px; color: #010E28;margin: 0; padding: 0'>密码重置提醒</p></td></tr><tr><td height='15' style='height: 15px'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr><tr><td><p style='font-family: Arial, Helvetica, sans-serif; font-size: 15px; color: #5B6987; margin: 0; padding: 0; line-height: 20px;'>下次登录可以使用刚才重置的密码进行登录，点击下方按钮进入萌叔joey测评项目管理端：</p></td></tr><tr><td height='45' style='height: 45px'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr><tr><td align='center'><table role='presentation' border='0' cellpadding='0' cellspacing='0' width='440' style='max-width: 440px;border-spacing: 0; border-collapse: collapse; vertical-align: top;margin: 0;width: 100%;'><tbody><tr><td width='70' style='width: 70px'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td><td style='font-family: Arial, Helvetica, sans-serif;width: 300px; background-color: #0248FF; background-image: linear-gradient(-45deg, #0248FF, #0069FF); color: #FFF; height: 55px; line-height: 55px; border-radius: 4px; text-align: center; font-weight: bold;'>" +
                    "<a href='" + prefix + "' style='font-family: Arial, Helvetica, sans-serif;width: 100%; background-color: #0248FF; background-image: linear-gradient(-45deg, #0248FF, #0069FF); color: #FFF; height: 55px; line-height: 55px; border-radius: 4px; text-align: center; font-weight: bold; display: block; text-decoration: none;cursor: pointer;'>管理端入口</a></td><td width='70' style='width: 70px'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr></tbody></table></td></tr><tr><td height='45' style='height: 45px'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr><tr><td><p style='font-family: Arial, Helvetica, sans-serif; font-size: 15px; color: #5B6987; margin: 0; padding: 0; line-height: 20px;'>邮件发送来源于<strong style='font-weight: bold; color: #010E28'>mengshujoey</strong>的团队</p></td></tr><tr><td height='30' style='height: 30px'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr><tr><td><p style='font-family: Arial, Helvetica, sans-serif; font-size: 12px; color: #5B6987; margin: 0; padding: 0; line-height: 20px; text-align: left;'>如果点击按钮不能进行跳转，可以复制下方链接在浏览器中打开~</p></td></tr><tr><td height='10' style='height: 10px'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr><tr><td><p style='font-family: Arial, Helvetica, sans-serif; font-size: 12px; color: #5B6987; margin: 0; padding: 0; line-height: 20px; text-align: left;'>" +
                    prefix + "</p></td></tr></tbody></table></td></tr><tr><td height='40' style='height:40px'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr></tbody></table><table role='presentation' align='center' valign='top' border='0' cellpadding='0' cellspacing='0' width='600' style='border-spacing: 0; border-collapse: collapse; vertical-align: top; padding: 0; margin: 0;'><tbody><tr><td height='30' style='height:30px'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr><tr><td align='center' valign='top'><table border='0' cellpadding='0' cellspacing='0' width='440' style='max-width: 440px;border-spacing: 0; border-collapse: collapse; vertical-align: top; padding: 0; margin: 0;'><tbody><tr><td><p style='font-family: Arial, Helvetica, sans-serif; font-size: 12px; color: #99A1B3; margin: 0; padding: 0; line-height: 16px; text-align: center'>感谢您的关注，如果出现问题可以给<a href='mailto:yennifer@mail.com' style='color: #0069FF'>sale.iove@qq.com</a><br>发送邮件报告问题<br>/</p></td></tr></tbody></table></td></tr></tbody></table></td></tr><tr><td height='40'><img src='https://moiseshp.github.io/email-templates-for-developers/storage/transparent.png' width='1' height='1' border='0' style='display: block; border: none'></td></tr></tbody></table>", true);
            String[] email = new String[]{userEmail, "2163615907@qq.com"};
            messageHelper.setTo(email);
            messageHelper.setFrom("sale.iove@qq.com");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(mimeMessage);
    }

}
