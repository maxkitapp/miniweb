# Component Sample

## text

單行輸入框，輸入框的鍵盤可以指定

### text - 範例圖片

![](./pics/demo_text.png)

### text - KoKoLa Request Sample

	POST http://192.168.1.88:8880/miniweb/rest/weather/app HTTP/1.1
	Host: 192.168.1.88:8880
	Proxy-Connection: keep-alive
	Content-length: 173
	Postman-Token: 99b55578-0571-6e37-4cf2-3c0f1b84ff38
	Cache-Control: no-cache
	Origin: chrome-extension://fhbjgbiflinjbdggehcddcbncdddomop
	User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36
	Content-Type: application/json
	Accept: */*
	Accept-Encoding: gzip, deflate
	Accept-Language: zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4
	
	{
	    "userid": "mayer",
	    "appuseruuid": "foo-bar",
	    "pagename" : "helperdetail",
	    "sessionid" : "",
	    "postdata" : [
	    	{"id": "item", "value": "text"}
	    ]
	}

### text - Third party Server Response Sample

	HTTP/1.1 200 
	Content-Type: application/json;charset=UTF-8
	X-Transfer-Encoding: chunked
	Date: Tue, 21 Feb 2017 09:59:39 GMT
	Content-length: 728
	
	{
	  "rcode": "200",
	  "rdesc": "ok",
	  "pagename": "helperdetail",
	  "returnpage": "helper",
	  "canforward": false,
	  "size": 18,
	  "body": [
	    {
	      "type": "text",
	      "value": "Text, Keyboard = any",
	      "keyboard": "any",
	      "size": 0
	    },
	    {
	      "type": "text",
	      "value": "Text, Keyboard = en",
	      "keyboard": "en",
	      "size": 0
	    },
	    {
	      "type": "text",
	      "value": "Text, Keyboard = digit",
	      "keyboard": "digit",
	      "size": 0
	    },
	    {
	      "type": "text",
	      "value": "Text, Keyboard = en_digit",
	      "keyboard": "en_digit",
	      "size": 0
	    },
	    {
	      "type": "text",
	      "value": "Text, Keyboard = email",
	      "keyboard": "email",
	      "size": 0
	    },
	    {
	      "type": "text",
	      "value": "Text, Keyboard = date",
	      "keyboard": "date",
	      "size": 0
	    },
	    {
	      "type": "text",
	      "value": "Text, Keyboard = time",
	      "keyboard": "time",
	      "size": 0
	    },
	    {
	      "type": "text",
	      "value": "Text, Keyboard = datetime",
	      "keyboard": "datetime",
	      "size": 0
	    }
	  ]
	}

## textarea

多行輸入框，輸入框的鍵盤可以指定

### textarea - 範例圖片

![](./pics/demo_textarea.png)

### textarea - KoKoLa Request Sample

	POST http://192.168.1.88:8880/miniweb/rest/weather/app HTTP/1.1
	Host: 192.168.1.88:8880
	Proxy-Connection: keep-alive
	Content-length: 177
	Postman-Token: e3a5c4a5-d831-2b2d-21eb-a4592e4bc986
	Cache-Control: no-cache
	Origin: chrome-extension://fhbjgbiflinjbdggehcddcbncdddomop
	User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36
	Content-Type: application/json
	Accept: */*
	Accept-Encoding: gzip, deflate
	Accept-Language: zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4
	
	{
	    "userid": "mayer",
	    "appuseruuid": "foo-bar",
	    "pagename" : "helperdetail",
	    "sessionid" : "",
	    "postdata" : [
	    	{"id": "item", "value": "textarea"}
	    ]
	}

### textarea - Third party Server Response Sample

	HTTP/1.1 200 
	Content-Type: application/json;charset=UTF-8
	X-Transfer-Encoding: chunked
	Date: Tue, 21 Feb 2017 10:00:24 GMT
	Content-length: 195
	
	{
	  "rcode": "200",
	  "rdesc": "ok",
	  "pagename": "helperdetail",
	  "returnpage": "helper",
	  "canforward": false,
	  "size": 18,
	  "body": [
	    {
	      "type": "textarea",
	      "value": "Textarea, Keyboard = any",
	      "keyboard": "any",
	      "size": 0
	    }
	  ]
	}

## option

單選選項，選項的圖可以自訂

### option - 範例圖片

![](./pics/demo_option.png)

### option - KoKoLa Request Sample

	POST http://192.168.1.88:8880/miniweb/rest/weather/app HTTP/1.1
	Host: 192.168.1.88:8880
	Proxy-Connection: keep-alive
	Content-length: 175
	Postman-Token: 9d20ab60-f357-5e53-00d3-0199b9b1bcae
	Cache-Control: no-cache
	Origin: chrome-extension://fhbjgbiflinjbdggehcddcbncdddomop
	User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36
	Content-Type: application/json
	Accept: */*
	Accept-Encoding: gzip, deflate
	Accept-Language: zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4
	
	{
	    "userid": "mayer",
	    "appuseruuid": "foo-bar",
	    "pagename" : "helperdetail",
	    "sessionid" : "",
	    "postdata" : [
	    	{"id": "item", "value": "option"}
	    ]
	}

### option - Third party Server Response Sample

	HTTP/1.1 200 
	Content-Type: application/json;charset=UTF-8
	X-Transfer-Encoding: chunked
	Date: Tue, 21 Feb 2017 10:04:48 GMT
	Content-length: 6951
	
	{
	  "rcode": "200",
	  "rdesc": "ok",
	  "pagename": "helperdetail",
	  "returnpage": "helper",
	  "canforward": false,
	  "size": 18,
	  "body": [
	    {
	      "type": "option",
	      "id": "optionid1",
	      "value": "請選擇選項：",
	      "optionlist": [
	        {
	          "optname": "選項一",
	          "optvalue": "o1",
	          "optimgid": null
	        },
	        {
	          "optname": "選項二",
	          "optvalue": "o2",
	          "optimgid": null
	        }
	      ],
	      "size": 18
	    },
	    {
	      "type": "option",
	      "id": "optionid2",
	      "value": "請選擇選項(有圖片)：",
	      "optionlist": [
	        {
	          "optname": "選項三",
	          "optvalue": "o3",
	          "optimgid": "o3"
	        },
	        {
	          "optname": "選項四",
	          "optvalue": "o4",
	          "optimgid": "o4"
	        }
	      ],
	      "size": 18
	    }
	  ],
	  "imgbody": [
	    {
	      "imgid": "o3",
	      "imgdata": "iVBORw0KGgoAAAANSUhEUgAA..."
	    },
	    {
	      "imgid": "o4",
	      "imgdata": "iVBORw0KGgoAAAANSUhEUgAA..."
	    }
	  ]
	}

## checkbox

多選選項，選項的圖可以自訂

### checkbox - 範例圖片

![](./pics/demo_checkbox.png)

### checkbox - KoKoLa Request Sample

	POST http://192.168.1.88:8880/miniweb/rest/weather/app HTTP/1.1
	Host: 192.168.1.88:8880
	Proxy-Connection: keep-alive
	Content-length: 177
	Postman-Token: e45ba71c-061d-d25b-8dc7-1cd904b9b45c
	Cache-Control: no-cache
	Origin: chrome-extension://fhbjgbiflinjbdggehcddcbncdddomop
	User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36
	Content-Type: application/json
	Accept: */*
	Accept-Encoding: gzip, deflate
	Accept-Language: zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4
	
	{
	    "userid": "mayer",
	    "appuseruuid": "foo-bar",
	    "pagename" : "helperdetail",
	    "sessionid" : "",
	    "postdata" : [
	    	{"id": "item", "value": "checkbox"}
	    ]
	}

### checkbox - Third party Server Response Sample

	HTTP/1.1 200 
	Content-Type: application/json;charset=UTF-8
	X-Transfer-Encoding: chunked
	Date: Tue, 21 Feb 2017 10:01:57 GMT
	Content-length: 6951
	
	{
	  "rcode": "200",
	  "rdesc": "ok",
	  "pagename": "helperdetail",
	  "returnpage": "helper",
	  "canforward": false,
	  "size": 18,
	  "body": [
	    {
	      "type": "checkbox",
	      "id": "checkid",
	      "value": "請勾選選項：",
	      "optionlist": [
	        {
	          "optname": "選項一",
	          "optvalue": "c1",
	          "optimgid": null
	        },
	        {
	          "optname": "選項二",
	          "optvalue": "c2",
	          "optimgid": null
	        }
	      ],
	      "size": 18
	    },
	    {
	      "type": "checkbox",
	      "id": "checkid",
	      "value": "請勾選選項(有圖片)：",
	      "optionlist": [
	        {
	          "optname": "選項三",
	          "optvalue": "c3",
	          "optimgid": "c3"
	        },
	        {
	          "optname": "選項四",
	          "optvalue": "c4",
	          "optimgid": "c4"
	        }
	      ],
	      "size": 18
	    }
	  ],
	  "imgbody": [
	    {
	      "imgid": "c3",
	      "imgdata": "iVBORw0KGgoAAAANSUhEUgAA..."
	    },
	    {
	      "imgid": "c4",
	      "imgdata": "iVBORw0KGgoAAAANSUhEUgAAADIA..."
	    }
	  ]
	}

## span

單純顯示文字描述的元件，文字可以更換字體大小、顏色、背景顏色

### span - 範例圖片

![](./pics/demo_span.png)

### span - KoKoLa Request Sample

	POST http://192.168.1.88:8880/miniweb/rest/weather/app HTTP/1.1
	Host: 192.168.1.88:8880
	Proxy-Connection: keep-alive
	Content-length: 173
	Postman-Token: cc92edc2-ccfe-9f35-03c2-8d71d045ce04
	Cache-Control: no-cache
	Origin: chrome-extension://fhbjgbiflinjbdggehcddcbncdddomop
	User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36
	Content-Type: application/json
	Accept: */*
	Accept-Encoding: gzip, deflate
	Accept-Language: zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4
	
	{
	    "userid": "mayer",
	    "appuseruuid": "foo-bar",
	    "pagename" : "helperdetail",
	    "sessionid" : "",
	    "postdata" : [
	    	{"id": "item", "value": "span"}
	    ]
	}

### span - Third party Server Response Sample

	HTTP/1.1 200 
	Content-Type: application/json;charset=UTF-8
	X-Transfer-Encoding: chunked
	Date: Tue, 21 Feb 2017 09:54:20 GMT
	Content-length: 256
	
	{
	  "rcode": "200",
	  "rdesc": "ok",
	  "pagename": "helperdetail",
	  "returnpage": "helper",
	  "canforward": false,
	  "size": 18,
	  "body": [
	    {
	      "type": "span",
	      "value": "span",
	      "size": 0
	    },
	    {
	      "type": "span",
	      "value": "span with color and bgcolor",
	      "size": 24,
	      "color": "#D1690E",
	      "bgcolor": "#E6E6FA"
	    }
	  ]
	}

## img

圖片顯示

### img - 範例圖片

![](./pics/demo_img.png)

### img - KoKoLa Request Sample

	POST http://192.168.1.88:8880/miniweb/rest/weather/app HTTP/1.1
	Host: 192.168.1.88:8880
	Proxy-Connection: keep-alive
	Content-length: 172
	Postman-Token: d2780731-13e2-933c-2f20-d897712dd779
	Cache-Control: no-cache
	Origin: chrome-extension://fhbjgbiflinjbdggehcddcbncdddomop
	User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36
	Content-Type: application/json
	Accept: */*
	Accept-Encoding: gzip, deflate
	Accept-Language: zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4
	
	{
	    "userid": "mayer",
	    "appuseruuid": "foo-bar",
	    "pagename" : "helperdetail",
	    "sessionid" : "",
	    "postdata" : [
	    	{"id": "item", "value": "img"}
	    ]
	}

### img - Third party Server Response Sample

	HTTP/1.1 200 
	Content-Type: application/json;charset=UTF-8
	X-Transfer-Encoding: chunked
	Date: Tue, 21 Feb 2017 10:02:46 GMT
	Content-length: 69545
	
	{
	  "rcode": "200",
	  "rdesc": "ok",
	  "pagename": "helperdetail",
	  "returnpage": "helper",
	  "canforward": false,
	  "size": 18,
	  "body": [
	    {
	      "type": "img",
	      "size": 0,
	      "imgid": "homepic"
	    }
	  ],
	  "imgbody": [
	    {
	      "imgid": "homepic",
	      "imgdata": "iVBORw0KGgoAAAANSUhEUgA..."
	    }
	  ]
	}