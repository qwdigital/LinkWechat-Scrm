package com.linkwechat.domain.wecom.query.msg;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author danmo
 */
@Data
public class WeAppMsgQuery extends WeBaseQuery {

    /**
     * 指定接收消息的成员，成员ID列表（多个接收者用‘|’分隔，最多支持1000个）。
     * 特殊情况：指定为”@all”，则向该企业应用的全部成员发送
     */
    private String touser;

    /**
     * 指定接收消息的部门，部门ID列表，多个接收者用‘|’分隔，最多支持100个。
     * 当touser为”@all”时忽略本参数
     */
    private String toparty;

    /**
     * 指定接收消息的标签，标签ID列表，多个接收者用‘|’分隔，最多支持100个。
     * 当touser为”@all”时忽略本参数
     */
    private String totag;

    /**
     * 消息类型，此时固定为：text
     */
    private String msgtype;

    /**
     * 企业应用的id，整型。企业内部开发，可在应用的设置页面查看；第三方服务商，可通过接口
     * <a href="https://work.weixin.qq.com/api/doc/10975#%E8%8E%B7%E5%8F%96%E4%BC%81%E4%B8%9A%E6%8E%88%E6%9D%83%E4%BF%A1%E6%81%AF">获取企业授权信息</a> 获取该参数值
     */
    private String agentid;

    /**
     * 表示是否是保密消息，0表示否，1表示是，默认0
     */
    private Integer safe;

    /**
     * 表示是否开启id转译，0表示否，1表示是，默认0
     */
    private Integer enable_id_trans;

    /**
     * 表示是否开启重复消息检查，0表示否，1表示是，默认0
     */
    private Integer enable_duplicate_check;

    /**
     * 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时
     */
    private Long duplicate_check_interval;

    /**
     *  根据msgtype的值不同添加一个属性和对应的属性值
     */

    /**
     * 文本消息
     */
    private Text text;
    /**
     * 图片消息
     */
    private Image image;
    /**
     * 语音消息
     */
    private Voice voice;
    /**
     * 视频消息
     */
    private Video video;
    /**
     * 文件消息
     */
    private File file;
    /**
     * 文本卡片消息
     */
    private TextCard textcard;
    /**
     * 图文消息
     */
    private News news;
    /**
     * 图文消息(mpnews)
     */
    private MpNews mpnews;
    /**
     * markdown消息
     */
    private Markdown markdown;
    /**
     * miniprogram_notice小程序通知
     */
    private MiniprogramNotice miniprogram_notice;
    /**
     * 任务卡片通知
     */
    private TaskCard taskcard;

    /**
     * 文本消息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class Text {
        /**
         * 消息内容，最长不超过2048个字节，超过将截断（支持id转译）
         */
        private String content;
    }

    /**
     * 图片消息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class Image {
        /**
         * 图片媒体文件id，可以调用上传临时素材接口获取
         */
        private String media_id;
    }

    /**
     * 语音消息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class Voice {
        /**
         * 语音文件id，可以调用上传临时素材接口获取
         */
        private String media_id;
    }

    /**
     * 视频消息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class Video {
        /**
         * 视频媒体文件id，可以调用上传临时素材接口获取
         */
        private String media_id;
        /**
         * 视频消息的标题，不超过128个字节，超过会自动截断
         */
        private String title;
        /**
         * 视频消息的描述，不超过512个字节，超过会自动截断
         */
        private String description;
    }

    /**
     * 文件消息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class File {
        /**
         * 文件id，可以调用上传临时素材接口获取
         */
        private String media_id;
    }

    /**
     * 文本卡片消息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class TextCard {
        /**
         * 标题，不超过128个字节，超过会自动截断（支持id转译）
         */
        private String title;
        /**
         * 描述，不超过512个字节，超过会自动截断（支持id转译）
         */
        private String description;
        /**
         * 点击后跳转的链接。
         */
        private String url;
        /**
         * 按钮文字。 默认为“详情”， 不超过4个文字，超过自动截断。
         */
        private String btntxt;
    }

    /**
     * 图文消息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class News{
        /**
         * 图文消息，一个图文消息支持1到8条图文
         */
        private List<Article> articles;
    }

    /**
     * 图文消息（mpnews）
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class MpNews {
        /**
         * 图文消息，一个图文消息支持1到8条图文
         */
        private List<Article> articles;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class Article {
        /**
         * 标题，不超过128个字节，超过会自动截断（支持id转译）
         */
        private String title;
        /**
         * 描述，不超过512个字节，超过会自动截断（支持id转译）
         */
        private String description;
        /**
         * 点击后跳转的链接。
         */
        private String url;
        /**
         * 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。
         */
        private String picurl;

        /**
         * 小程序appid，必须是与当前应用关联的小程序，appid和pagepath必须同时填写，填写后会忽略url字段
         */
        private String appid;

        /**
         * 点击消息卡片后的小程序页面，最长128字节，仅限本小程序内的页面。appid和pagepath必须同时填写，填写后会忽略url字段
         */
        private String pagepath;
        /**
         * 图文消息缩略图的media_id, 可以通过素材管理接口获得。此处thumb_media_id即上传接口返回的media_id
         */
        private String thumb_media_id;
        /**
         * 图文消息的作者，不超过64个字节
         */
        private String author;
        /**
         * 图文消息点击“阅读原文”之后的页面链接
         */
        private String content_source_url;
        /**
         * 图文消息的内容，支持html标签，不超过666 K个字节（支持id转译）
         */
        private String content;
        /**
         * 图文消息的描述，不超过512个字节，超过会自动截断（支持id转译）
         */
        private String digest;
    }

    /**
     * markdown消息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class Markdown {
        /**
         * markdown内容，最长不超过2048个字节，必须是utf8编码
         */
        private String content;
    }

    /**
     * 小程序通知消息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class MiniprogramNotice {
        /**
         * 小程序appid，必须是与当前小程序应用关联的小程序
         */
        private String appid;
        /**
         * 击消息卡片后的小程序页面，仅限本小程序内的页面。该字段不填则消息点击后不跳转。
         */
        private String page;
        /**
         * 消息标题，长度限制4-12个汉字（支持id转译）
         */
        private String title;
        /**
         * 消息描述，长度限制4-12个汉字（支持id转译）
         */
        private String description;
        /**
         * 是否放大第一个content_item
         */
        private String emphasis_first_item;
        /**
         * 消息内容键值对，最多允许10个item {长度10个汉字以内key,长度10个汉字以内value}
         */
        private List<Map<String, Object>> content_item;
    }

    /**
     * 任务卡片消息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class TaskCard {
        /**
         * 标题，不超过128个字节，超过会自动截断（支持id转译）
         */
        private String title;
        /**
         * 描述，不超过512个字节，超过会自动截断（支持id转译）
         */
        private String description;
        /**
         * 点击后跳转的链接。最长2048字节，请确保包含了协议头(http/https)
         */
        private String url;
        /**
         * 任务id，同一个应用发送的任务卡片消息的任务id不能重复，只能由数字、字母和“_-@”组成，最长支持128字节
         */
        private String task_id;
        /**
         * 按钮列表，按钮个数为1~2个
         */
        private List<Button> btn;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class Button {
        /**
         * 按钮key值，用户点击后，会产生任务卡片回调事件，回调事件会带上该key值，只能由数字、字母和“_-@”组成，最长支持128字节
         */
        private String key;
        /**
         * 按钮名称
         */
        private String name;
        /**
         * 点击按钮后显示的名称，默认为“已处理”
         */
        private String replace_name;
        /**
         * 按钮字体颜色，可选“red”或者“blue”,默认为“blue”
         */
        private String color;
        /**
         * 按钮字体是否加粗，默认false
         */
        private Boolean is_bold;
    }
}
