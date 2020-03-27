# 获取用户基本信息（UnionID机制）

#### 获取用户基本信息（UnionID机制）

在关注者与公众号产生消息交互后，公众号可获得关注者的OpenID（加密后的微信号，每个用户对每个公众号的OpenID是唯一的。对于不同公众号，同一用户的openid不同）。公众号可通过本接口来根据OpenID获取用户基本信息，包括昵称、头像、性别、所在城市、语言和关注时间。

请注意，如果开发者有在多个公众号，或在公众号、移动应用之间统一用户账号的需求，需要前往微信开放平台（open weixin.qq.com）绑定公众号后，才可利用UnionID机制来满足上述需求。

#### UnionID机制说明：

开发者可通过OpenID来获取用户基本信息。特别需要注意的是，如果开发者拥有多个移动应用、网站应用和公众账号，可通过获取用户基本信息种的unionid来区分用户的唯一性，因为只要是同一个微信开放平台账号下的移动应用、网站应用和公众账号，用户的unionid是唯一的。换句话说，同一用户，对同一个微信开放平台下的不同应用，unionid是相同的。

#### 获取用户基本信息（包括UnionID机制）

开发者可通过OpenID来获取用户基本信息。请使用https协议。

```java
接口调用请求说明
http请求方式：GET
https://api.weixin.qq.com/cgi-bin/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
```

**参数说明**

| 参数         | 是否必须 | 说明                                    |
| ------------ | -------- | --------------------------------------- |
| access_token | 是       | 调用接口凭证                            |
| openid       | 是       | 普通用户的标识，对当前公众号唯一        |
| lang         | 否       | 返回国家地区语言版本，zh_CN简体，en英语 |

**返回说明**

正常情况下，微信会返回下述JSON数据包给公众号：

```json
{
	"subscribe": 1,
  "openid": "o6_bmjrPT1m6_2sgVt7hMZOPfL2M",
  "nickname": "Band",
  "sex": 1,
  "lang": "zh_CN",
  "city": "广州",
  "country": "中国",
  "headimgurl": "www.baidu.com",
  "subscribe_time": 13826944,
  "unionid": "UNIONID",
  "remark": "",
  "groupid": 0,
  "tagid_list": [128, 2],
  "subscribe_scene": "ADD_SCENE_QR_CODE",
  "qr_scene": 98765,
  "qr_scene_str": ""
}
```

错误时微信会返回错误码等信息，JSON数据包示例如下（该示例为AppID无效错误）：

```json
{ "errcode": 40013, "errmsg": "invalid appid" }
```

#### 批量获取用户基本信息

开发者可通过该接口来批量获取用户基本信息。最多支持一次拉取100条。

**接口调用请求说明**

```java
http请求方式：POST
https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN
```

POST数据示例

```json
{
	"user_list": [
    {
      "openid": "otvxTs4dckWG7imySrJd6jSi0CWE",
      "lang": "zh_CN"
    },
    {
      "openid": "otvxTs_JZ6SEiP0imdhpi50fuSZg",
      "lang": "zh_CN"
    }
  ]
}
```

**参数说明**

| 参数   | 是否必须 | 说明                         |
| ------ | -------- | ---------------------------- |
| openid | 是       | 用户的标识，对当前公众号唯一 |
| lang   | 否       | 国家地区语言版本             |

**返回说明**

正常情况下，微信会返回下述JSON数据包给公众号（示例中为一次性拉去了2个openid的用户基本信息，第一个是以关注的，第二个是未关注的）：

```json
{
  "user_info_list": [
    {
      "subscribe": 1,
      "openid": "OPENID",
      "nickname": "iWithery",
      "sex": 1,
      "language": "zh_CN",
      "city": "揭阳",
      "province": "广州",
      "country": "中国",
      
      "headimgurl": "http://www.baidu.com",
      
      "subscribe_time": 1433425234,
      "unionid": "oR5GjjgEhCMJFyZdrdwj45sgjg",
      "remark": "",
      
      "groupid": 0,
      "tagid_list": [128, 2],
      "subscribe_scene": "ADD_SCENE_QR_CODE",
      "qr_scene": 98765,
      "qr_scene_str": ""
    },
    {
      "subscribe": 0,
      "openid": "otvxTs_JZ6SEiP0imdhpi50fuSZg"
    }
  ]
}
```

**参数说明**

| 参数            | 说明                                                         |
| --------------- | ------------------------------------------------------------ |
| subscribe       | 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余数据。 |
| openid          | 用户的标识，对当前公众号唯一                                 |
| nickname        | 用户的昵称                                                   |
| sex             | 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知      |
| city            | 用户所在城市                                                 |
| country         | 用户所在国家                                                 |
| province        | 用户所在省份                                                 |
| language        | 用户的语言，简体中文为zh_CN                                  |
| headimgurl      | 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。 |
| subscribe_time  | 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间 |
| unionid         | 只有在用户将公众号绑定到微信开放平台账号后，才会出现该字段   |
| remark          | 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注 |
| groupid         | 用户所在的分组ID（暂时兼容用户分组旧接口）                   |
| tagid_list      | 用户被打上的标签ID列表                                       |
| subscribe_scene | 返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATON，ADD_SCENE_PROFILE_CARD名片分享，ADD_SCENE_QR_CODE扫描二维码，ADD_SCENE_PROFILE_LINK图文页内名称点击，ADD_SCENE_PROFILE_ITEM图文页右上角菜单，ADD_SCENE_PAID支付后关注，ADD_SCENE_OTHERS其他 |
| qr_scene        | 二维码扫描场景（开发者自定义）                               |
| qr_scene_str    | 二维码扫码场景描述（开发者自定义）                           |

错误时微信会返回错误码等信息，JSON数据包示例如下（该示例为AppID无效错误）：

```json
{ "errcode": 40013, "errmsg": "invalid appid" }
```