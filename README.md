# S3_storage
AWS S3 이용하기


memberId 같은 경우는 민감한 정보여서 request header에 넣는것이 맞으나 실력 이슈로 구현하지 못했습니다. 양해부탁드립니다 !!~!~!

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
    
    ![스크린샷 2024-05-16 오전 11.41.47.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/e8ad1f50-1978-48e5-855d-38652e2bcb89/c8a9781f-cad0-4a57-bcb1-947a7ec21a15/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-05-16_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_11.41.47.png)
    
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
    
    ![스크린샷 2024-05-16 오전 11.41.47.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/e8ad1f50-1978-48e5-855d-38652e2bcb89/c8a9781f-cad0-4a57-bcb1-947a7ec21a15/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-05-16_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_11.41.47.png)
    
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
    
    ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/e8ad1f50-1978-48e5-855d-38652e2bcb89/0ef2f8df-a649-4326-9f99-eee574856051/Untitled.png)
    
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
    
    ![스크린샷 2024-05-16 오전 11.57.01.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/e8ad1f50-1978-48e5-855d-38652e2bcb89/0d1c0dfc-44fa-46d4-a59f-8f7ea400548f/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-05-16_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_11.57.01.png)
    
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
    
    ![스크린샷 2024-05-16 오전 11.59.07.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/e8ad1f50-1978-48e5-855d-38652e2bcb89/177f1d45-9149-4f38-b2e0-959611c3f338/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-05-16_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_11.59.07.png)
