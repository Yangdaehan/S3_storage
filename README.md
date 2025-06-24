# S3_storage
AWS S3 storage을 관리하는 API 입니다.
 ![SSI_20180914171905_O2](https://github.com/user-attachments/assets/c3cdc9b7-9061-4dc6-9eb9-97158a6b6e98)

 &nbsp;
 
## 📋 구현 기능 명세
파일 저장, 조회, 삭제 하는 API를 구현했습니다.

- [POST]   파일 업로드 API -> `uploadFiles`
- [GET]    파일 다운로드 API -> `downloadFile`
- [GET]    바이너리 파일 다운로드 API -> `downloadBinaryFile`
- [GET]    파일 및 멤버 조회 API -> `list`
- [DELETE] 파일 삭제 API -> `deleteFile`
- [DELETE] 폴더 삭제 API -> `deleteFolder`

[API 명세서](https://documenter.getpostman.com/view/31445434/2sA3QtcqQx)
 
 &nbsp;
 
## 📢 기능 상세 설명
<!-- 다음에 진행할 작업에 대해 작성해주세요 -->
> 💡  S3 데이터 모델은 폴더 구조를 가지지 않고, 접두사를 통해 계층구조를 표현합니다. 하지만 우리가 AWS S3 페이지에서 보는 콘솔에서는 마치 폴더 구조를 가진 것 처럼 보여줍니다. 그래서 폴더라고 생각하고 구현했습니다.
 
 &nbsp;
 
### 1. 파일 업로드 API 구현 

memberId를 제공받아 memberId 폴더를 만들도록 구현했습니다. 그리고 하위 폴더 이름이 제공될 경우, 하위 폴더를 만들어 파일을 하위 폴더에 업로드 할 수 있게 구현했습니다.
https://github.com/Yangdaehan/S3_storage/blob/ecc5328f66ee30ae0916cdf6387221d8bd9f42d0/src/main/java/org/mse/s3_storage/com/controller/S3Controller.java#L28-L35

  
### 2. 파일 다운로드 API 구현

파일을 다운로드 하는 방식을 두가지를 구현했습니다. 

첫번째로 일반 파일을 url 형식으로 전송받아서 저장가능하게끔 구현했습니다. 예시는 아래와 같습니다.
![image](https://github.com/user-attachments/assets/d26e0448-134b-4c7b-9b40-5c9054e284f8)

두번째로는 바이너리 형식으로 받게끔 구현했습니다. 따라서 이미지 파일 같은 경우 바로 나타나집니다.
![image](https://github.com/user-attachments/assets/1cb0b58d-b124-42c1-8106-9b7323802e24)

### 3. 파일 및 멤버 조회 API 구현 

memberId와 하위 폴더 모두 required를 false로 설정하여 받아졌을때만 해당 기능을 구현하게끔 설정했습니다.
https://github.com/Yangdaehan/S3_storage/blob/ecc5328f66ee30ae0916cdf6387221d8bd9f42d0/src/main/java/org/mse/s3_storage/com/controller/S3Controller.java#L90-L93

### 4. 삭제 API 구현 

삭제를 두가지 방식으로 구현했습니다. 첫번재는 특정 파일만 삭제하게 구현했습니다. 
https://github.com/Yangdaehan/S3_storage/blob/ecc5328f66ee30ae0916cdf6387221d8bd9f42d0/src/main/java/org/mse/s3_storage/com/controller/S3Controller.java#L121-L125
파일 이름을 제공받아 삭제를 할 수 있게 구현했습니다.

두번째로는 폴더를 모두 삭제하는 기능입니다. 회문을 돌려 모든 파일이 삭제되게 구현했습니다.
https://github.com/Yangdaehan/S3_storage/blob/ecc5328f66ee30ae0916cdf6387221d8bd9f42d0/src/main/java/org/mse/s3_storage/com/service/AmazonS3Service.java#L168-L183
