# S3_storage
AWS S3 이용하기



memberId 같은 경우는 민감한 정보여서 request header에 넣는것이 맞으나 추후에 업데이트 하겠습니다.



- `postFile` (**POST** - 파일 업로드 )
    
## 1️⃣ 어떤 API 인가요?
    
S3 저장소에 파일(사진, 엑셀 등)을 memberId 레퍼지토리에 업로드 하는 api입니다. 
    
## 2️⃣ Request
    
### URL : ~ /{memberId}/photo
    
### Path Parameter
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
    
### Request Header
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
    
### Request Body
    
| 이름 | 타입 | Description |
| --- | --- | --- |
| profile_photo | jpg, xlsx | key - profile_photo, value - 파일을 넣어주시면 됩니다. |
|  |  |  |
|  |  |  |
    
---
    
## 3️⃣ Response
    
### **Code: 200 OK**
    
### ✨ Response Header
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
    
### ✨ Response Body
    
| 이름 | 타입 |
| --- | --- |
| s3 저장 url 반환 | url |
|  |  |
|  |  |
|  |  |
|  |  |
    

    
- `postFiles` (**POST** - 여러 파일 업로드 )
    
## 1️⃣ 어떤 API 인가요?
    
S3 저장소에 한번에 여러 파일들을 업로드 하는 api입니다.
    
## 2️⃣ Request
    
### URL : ~ /{memberId}/photo
    
### Path Parameter
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
    
### Request Header
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
    
### Request Body
    
| 이름 | 타입 | Description |
| --- | --- | --- |
| profile_photo | jpg, xlsx | key - profile_photo, value - 파일을 넣어주시면 됩니다. |
|  |  |  |
|  |  |  |
    
---
    
## 3️⃣ Response
    
### **Code: 200 OK**
    
### ✨ Response Header
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
    
### ✨ Response Body
    
| 이름 | 타입 |
| --- | --- |
| s3 저장 url 반환 | url |
|  |  |
|  |  |
|  |  |
|  |  |
    

    
- `getPhoto` (**GET** - 사진 파일 다운로드 )
    
## 1️⃣ 어떤 API 인가요?
    
S3 저장소에서 사진 파일을 다운 받는 api 입니다.
    
memberId 레퍼지토리에 있는 storedFileName 의 이름을 가진 이미지를 다운 받습니다.
    
바이너리 파일 형식으로 제공됩니다.
    
## 2️⃣ Request
    
### URL :  ~/{memberId}/{storedFileName}/image_download
    
Path Parameter
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |   |
|  |  |  |
    
### Request Header
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
    
### Request Body
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
|  |  |  |
    
---
    
### ✨ Response
    

    
- `getExcel`  (**GET** - 엑셀 파일 다운로드)
    
## 1️⃣ 어떤 API 인가요?
    
S3 저장소에 있는 엑셀 파일을 다운 받는 api 입니다. 
    
`getPhoto`와 차이점은 파일을 url 형식으로 반환한다는 것입니다.
    
## 2️⃣ Request
    
### URL :  ~/{memberId}/{storedFileName}/xlsx_download
    
### Path Parameter
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
    
### Request Header
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
    
### Request Body
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
|  |  |  |
    
---
    
## 3️⃣ Response
    

    
- `getList`  (**GET** - memberId 리스트 반환)
    
## 1️⃣ 어떤 API 인가요?
    
S3 저장소에 있는 memberId 들을 반환하는 api 입니다. 
    
## 2️⃣ Request
    
### URL :  ~/list
    
### Path Parameter
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
    
### Request Header
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
    
### Request Body
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
|  |  |  |
    
---
    
## 3️⃣ Response
    
 ### **Code: 200 OK**
    
### ✨ Response Header
    
| 이름 | 타입 | Description |
| --- | --- | --- |
|  |  |  |
|  |  |  |
    
### ✨ Response Body
    
| 이름 | 타입 |
| --- | --- |
| memberId 별 리스트 반환 | json |
|  |  |
|  |  |
|  |  |
|  |  |
    
