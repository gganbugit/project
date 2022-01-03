![image](https://user-images.githubusercontent.com/88874870/147899388-b55b9de3-ce2d-4ff8-a20d-5020e5244375.png)
## KoGPT2를 활용한 자기소개서 문장 생성기
## Speech Recognizer를 활용한 모의면접

# 1. 프로젝트 선정 개요
해마다 취업문은 좁아지고 취업준비생의 부담은 더 가중되고 있다. 취업의 첫 관문인 자기소개서를 작성하는 것이 쉽지 않기 때문이다. 한 취업 사이트 통계에 따르면 자기소개서 작성에 극심한 어려움을 느끼는 이른바 ‘자소서 포비아’를 겪는 구직자도 10명 중 8명인 것으로 집계됐다. 이러한 어려움을 해결하는 데 도움을 줄 수 있는 애플리케이션을 기획했다.

첫 번째로 자기소개서 작성 시 막히는 부분에서 키워드를 입력하면 AI가 문장을 작성해주는 서비스를 자연어 생성(NLP) 모델 중 KoGPT2를 선정하여 약10만개의 합격 자기소개서를 학습 후 어색하거나 문법에 맞지 않는 문장을 지양하고 사용자가 원하는 답변에 근접하도록 샘플링하여 활용하였습니다.

두 번째로 면접에 어려움을 느끼는 분들을 위한 면접트레이닝을 Android Speech Recognizer를 활용하여 실제 모의면접처럼 음성으로 질문이 전달되고 음성으로 답변한 부분을 텍스트로 지원하여 자신의 부족한 부분을 점검 및 보안 할 수 있도록 하였습니다.

마지막으로 서버를 통한 사용자의 로그인 정보를 바탕으로 DB에 저장된 본인의 자기소개서, 모의 면접의 저장된 기록들을 확인, 수정, 공유 기능을 통해 취업 준비에 실질적인 도움을 줄 수 있는 서비스 구현을 목표로 하였습니다.


![image](https://user-images.githubusercontent.com/88874870/147899493-9d694e7e-623e-415b-b0fa-5a76c30fba30.png)

# 2. 프로젝트 범위 및 구성
![image](https://user-images.githubusercontent.com/88874870/147899667-3c14c569-9300-46e7-b188-ac38e5bb7b72.png)


# 3. 프로젝트 결과
 *  3.1 모델 아키텍처 (Model Architecture)
 
![image](https://user-images.githubusercontent.com/88874870/147899748-bda448ce-b657-49b2-a626-c056f14cb7c4.png)

[모델선정]
여러 자연어 모델 중, 자연어 생성 모델 중에서 좋은 성능을 보이고 있고 한국어가 pretrained 되어있으며, 올해 5월에 version-2를 선보인 KoGPT2모델로 선정하였다. KoGPT2 모델은 한국어 위키백과 이외, 뉴스, 모두의 말뭉치 v1.0, 청와대 국민청원 등 다양한 데이터가 학습되어 있다.

[Crawling]
Linkareer.com 사이트의 합격자기소개서 10,522건 전체를 크롤링 하였다. xpath를 이용하여 한 페이지 안의 20건의 자기소개서를 모두 크롤링 후 다음 페이지로 이동하여 크롤링 하는 방식으로 진행하였다.

[Data Processing]
1차 전처리 : 정규식을 이용하여 공통되는 부분을 삭제 후, readlines을 이용해 문장 단위 자르기
2차 전처리 : 자기소개서 질문과 소제목은 수작업으로 제거하였다. 우선, 엑셀의 중복된 항목 제거로 완벽히 똑같은 문장이 있을시 제거 후, 메모장을 이용하여 수기 제거 작업을 하였다. 한자와 정치적 편향이 심한 내용 모두 제거하였다. 전처리 후 183,854줄로 전처리를 완료하였다.

[Training]
전처리된 데이터를 기반으로 fine-tuning training을 진행하였다. batch size는 10에서 100까지 사이의 값을 넣었을 때 가장 효과가 있는 것으로 알려져 있어, 17로 배치사이즈를 지정하였다. 우선 1epoch 실행후, 5epoch, 15epoch를 순차적으로 진행하여 time, loss, 성능을 비교할 수 있도록 하였다. 최종 15epoch로 학습했을 시, 총 31시간 소요, 2.17까지 loss를 낮추었다.

[Text Generation]
앞서 train된 model, tokenizer를 가져와서 샘플링 후 문장을 생성시켰다. k개의 토큰을 뽑는 top_k는 40으로, 누적 분포 값으로 계산하는 top_p는 0.95로 정확한 답변을 얻을 수 있도록 샘플링 하였다.

 *  3.2 애플리케이션 (Application)

 애플리케이션은 Android를 구현하기 위해 코틀린(Kotlin) 언어를 활용하였다. Server(Django)와 통신을  하기 위해 Square사에서 제공하는 Retrofit2 라이브러리를 사용하였다. Retrofit2는 안드로이드에서 REST API 통신을 위해 구현된 통신 라이브러리이다. 
 
 ![image](https://user-images.githubusercontent.com/88874870/147907545-20b59c4f-6bd0-44d1-b454-779d884ab868.png)
 앱을 실행하면 [그림 1]과 같은 화면이 나온다. LOGIN 버튼과 SIGN UP 버튼은 각각 [그림 2] 로그인 화면과 [그림 3] 회원가입 화면으로 전환된다. 
 기존에 회원가입이 되어 있다면 [그림 2]에서 이메일과 비밀번호를 입력하고 LOG IN 버튼을 누르면 메인화면으로 전환되며, 로그인에 성공한 계정(이메일)에 쿠키가 생성된다. 생성된 쿠키는 로그아웃 전까지 세션이 유지된다. 
 회원가입이 되어 있지 않다면 [그림 3]에서 이메일과 비밀번호를 입력하고 SIGN UP버튼을 누르면 회원가입이 완료 되고, [그림 2] 로그인 화면으로 전환된다.
 
![image](https://user-images.githubusercontent.com/88874870/147907654-75d4b942-a069-45b3-8234-5f12a6d49267.png)

![image](https://user-images.githubusercontent.com/88874870/147907795-3454aca8-1699-498f-982b-884e051876e1.png)

[그림 4]에서 COVER NOTE 버튼을 클릭 하면 [그림 5] 화면으로 전환 된다. COVER NOTE 화면에 있는 버튼에 대한 설명을 화면 전환 시에 나타냄으로써, 사용자의 기능 이해도를 증진 했다. [그림 5]에서 우측 상단 X 버튼을 누르면 [그림 6]으로 전환 된다. 

![image](https://user-images.githubusercontent.com/88874870/147907847-1c198378-1fa5-4f47-890d-2b79e711cf2a.png)

키워드를 입력하고 돋보기 버튼을 클릭하면 [그림 7]과 같이 문장을 찾는 동안 사용자가 지루하지 않도록 커스텀 다이얼로그 창이 뜬다. 실제로는 구슬 이미지가 좌우로 움직이며, ‘문장을 생성중입니다’라는 문구가 깜빡거린다. 문장 생성을 완료 하면 [그림 8]과 같이 5개의 문장이 추천문장 1~5에 생성된다. 
 또한, 추천문장 하단에 자기소개서 제목과 내용에 텍스트를 입력하고 자기소개서 제목 창에 디스켓 버튼을 클릭 하면 작성한 자기소개서가 MY PAGE에 저장된다. 
 [그림 8]에서 하단에 보이는 4개의 버튼은 왼쪽부터 메인 화면, COVER NOTE, INTERVIEW, MY PAGE 버튼 이며, 현재는 COVER NOTE 화면이므로 두 번째 COVER NOTE 버튼은 비활성화 되어 있다. 
 
![image](https://user-images.githubusercontent.com/88874870/147908067-208f57e9-1a04-4c26-b935-ca8a24b21489.png)

 하단에서 제일 왼쪽에 있는 MY PAGE 버튼을 누르면 MY PAGE 화면으로 전환되며, 화면에는 현재 로그인 한 계정, 저장된 자기소개서 개수와 모의면접 개수가 표시 된다. 현재는 저장된 자기소개서가 1개, 모의면접 개수가 0개 이므로 [그림 9]와 같이 화면에 나타난다. 
 저장된 리스트를 클릭 하면 [그림 10] EDIT PAGE 화면으로 전환된다. EDIT PAGE에서는 제목 및 내용 수정, 자기소개서 삭제, 자기소개서 공유하기 기능이 구현되어 있다. 자기소개서 제목 창에 있는 우측 삼선 버튼을 누르면 저장, 삭제, 공유하기 팝업 메뉴가 뜬다. [그림 10]과 같이 자기소개서 제목, 내용을 수정하고 팝업 메뉴에서 저장 버튼을 누르면 수정된 내용으로 자기소개서가 저장된다. 
 삭제 버튼을 누르게 되면 [그림 11]과 같이 한번 더 삭제 여부를 확인하는 다이얼로그 창이 뜨며, 삭제 버튼을 누르면 저장되어 있는 자기소개서가 삭제된다. 공유하기 버튼을 누르면 [그림 12]와 같이 현재 스마트폰에 설치된 어플리케이션 중 공유 기능이 있는 어플리케이션 목록이 뜬다. 
 
 ![image](https://user-images.githubusercontent.com/88874870/147908009-53f4b151-d95d-41a0-9750-ccc09bfb12b2.png)
 
 INTERVIEW 화면도 COVER NOTE 화면과 마찬가지로, 진입 시 [그림 13]과 같이 INTERVIEW 화면에 대한 설명을 넣어주었다. 우측 상단에 있는 X 버튼을 누르면 [그림 14]로 전환된다.    
 QUESTION 문자 옆에 재생 버튼을 누르면 20개의 예상 질문 중 무작위로 1개의 질문이 TTS(Text To Speech) 로 변환된다. ANSWER 문자 옆에 버튼을 누르고 말을 하면 STT(Speech To Text)가 실행된다.  ANSWER 창 오른쪽 하단에 저장 버튼을 누르면 모의 면접이 저장되며, [그림 16]과 같이 모의면접이 저장된 것을 확인 할 수 있다. 하단에 4개의 아이콘 중 가장 오른쪽에 있는 아이콘은 로그아웃 버튼으로 버튼을 클릭하면 로그인 화면으로 전환되며, 로그인 시 유지되었던 세션이 종료된다. 저장된 모의면접 클릭 시 전환되는 EDIT PAGE 는 COVER NOTE 와 동일하다. 

 * 3.3 통신 순서 (Interconnection Process)
![image](https://user-images.githubusercontent.com/88874870/147908117-4bf3105f-9a7b-467d-a509-1e8465e2853d.png)

 본 프로젝트는 Django REST framework를 기반으로 서버를 구축하였으며, 안드로이드 앱에서 keyword를 JSON 형식으로 Server 측에 전송하고 Deep Learning Model에 input 값으로 넣어 모델이 새로운 문장을 만들어 output 값으로 서버에서 안드로이드로 만들어진 sentence들을 JSON 형식으로 송신 한다.

 * 3.4 데이터베이스 (Database)

![image](https://user-images.githubusercontent.com/88874870/147908163-915eb302-1092-4a57-83c8-57beee55af21.png)

 데이터베이스는 users, cover, interview의 세 개의 테이블로 구축하였다. 사용자가 회원가입에 사용한 e-mail과 password가 users 테이블에 등록된다. e-mail이 user_id 값으로 저장되어 cover 테이블과 interview 테이블에 내용이 등록될 때 user_id를 가져와 저장 될 수 있도록 하였다.
 사용자가 작성한 자기소개서 제목은 subject를, 내용은 content에 저장 되도록 하였으며, 모의면접 질문은 question에, 답변 내용은 answer에 저장되도록 하였다.
 
# 4. 개발도구 및 일정

 *  4.1 개발도구
  - 개발환경 : AWS EC2, Linux(Ubuntu 18.04), Window 10
  - 개발언어 : Python, Kotlin 
  - 사용기술 : Django Rest Framework, Rest API, TensorFlow, PyTorch, Retrofit2, Android Speech Recognizer
  - 개발도구 : Pycharm, Anaconda, Jupyter lab, Android Studio, Workbench, Insomnia, Mobaxterm
 
 * 4.2 개발일정
![image](https://user-images.githubusercontent.com/88874870/147908299-b2e10786-a12c-4151-8529-89cfe1c6a5fe.png)

 * 4.3 요구사항(Requirements)
![image](https://user-images.githubusercontent.com/88874870/147908381-0a6e3956-390d-4dd5-b112-52674d9af310.png)

# 5. 참고문헌

![image](https://user-images.githubusercontent.com/88874870/147908608-e8bca78c-86e0-4a9c-88a3-2cb4cea4a036.png)

# 6. 프로젝트 발전 방향

![image](https://user-images.githubusercontent.com/88874870/147908728-d30eba7e-45b9-4c9c-b67d-11eeaa10615a.png)








 




