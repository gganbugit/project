from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from .models import User
from django.contrib.auth.hashers import make_password, check_password
# Create your views here.

class AppLogin(APIView):
    def post(self, request):
        user_id = request.data.get('user_id', "")
        password = request.data.get('password', "")
        user = User.objects.filter(user_id=user_id).first()

        if user is None:
            return Response(dict(msg="해당 ID의 사용자가 없습니다."))

        if check_password(password, user.password) is False:
            return Response(dict(msg="로그인 실패. 패스워드 불일치"))

        request.session['loginCheck'] = True
        request.session['user_id'] = user.user_id
        
        user_id = request.session.get('user_id', None)
        print(user_id)

        return Response(dict(msg="로그인 성공", user_id=user.user_id))


class RegistUser(APIView):
    def post(self, request):

        user_id = request.data['user_id']
        # 아이디 공백 체크
        if user_id == '' or None:
            return Response(status=200, data=dict(msg="아이디는 공백이 될 수 없습니다!!"))

        password = request.data['password']
        # 패스워드 공백 체크
        if password == '' or None:
            return Response(status=200, data=dict(msg="비밀번호는 공백이 될 수 없습니다!!"))

        # 이미 존재하는 아이디인지 체크
        if User.objects.filter(user_id=user_id).exists():
            return Response(status=200, data=dict(msg="이미 존재하는 아이디 입니다."))

        # 암호화 해서 집어넣기
        user = User.objects.create(user_id=user_id, password=make_password(password))

        return Response(status=200, data=dict(msg="회원가입을 축하드립니다!", user_id=user.user_id))
        

class LogOut(APIView):
    def get(self, request):
        request.session.flush()
        return Response(status=200, data=dict(msg="로그아웃"))


