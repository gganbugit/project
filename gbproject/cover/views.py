from django.shortcuts import render
from rest_framework.views import APIView
from .models import Cover
from interview.models import Interview
from rest_framework.response import Response
from login.models import User
# Create your views here.


class Create(APIView):
    def post(self, request):
        user_id = request.session.get('user_id', None)
        cover_id = request.data.get('cover_id', None)      #삭제 추가 db와 숫자 맞춰줘야함
        subject = request.data.get('subject', "")
        content = request.data.get('content', "")

        if cover_id:
            cover = Cover.objects.create(id=cover_id, user_id=user_id, subject=subject, content=content)
        else:
            cover = Cover.objects.create(user_id=user_id, subject=subject, content=content)

        return Response(data=dict(cover_id=cover.id))


#        Cover.objects.create(user_id=user_id, subject=subject, content=content)
#        return Response(status=200, data=dict(message='저장 완료!'))

class Select(APIView):
    def get(self, request):
        user_id = request.session.get('user_id', None)
#        user = User.objects.filter(user_id=user_id)
#        covers = Cover.objects.all().order_by('-user_id')
        covers = Cover.objects.filter(user_id=user_id)
        interviews = Interview.objects.filter(user_id=user_id)
        interview_count = interviews.count()
        cover_list = []
        cover_count = covers.count()
        for cover in covers:
            cover_list.append(dict(user_id=user_id,
                                   interview_count=interview_count,
                                   cover_count=cover_count,
                                   cover_id=cover.id,
                                   subject=cover.subject,
                                   content=cover.content
                                   ))
        #return Response(dict(covers=cover_list))
        cover_list = sorted(cover_list, key=(lambda x: x['cover_id']), reverse=True)
        print(cover_list)
        if len(cover_list) == 0:
            cover_list.append(dict(user_id=user_id,
                                   interview_count=interview_count,
                                   cover_count=0,
                                   cover_id=0,
                                   subject=0,
                                   content=0
                                   ))
            return Response(dict(covers=cover_list))
        else:
#        if len(cover_list) != 0:
            return Response(dict(covers=cover_list))


class Update(APIView):
    def post(self, request):
        user_id = request.session.get('user_id', None)
        cover_id = request.data.get('cover_id', "")
        #subject = request.data.get('subject', "")
        #content = request.data.get('content', "")
        cover = Cover.objects.get(user_id=user_id,id=cover_id)
        cover.subject = request.data.get('subject', "")
        cover.content = request.data.get('content', "")
        cover.save()
        return Response(status=200, data=dict(msg="수정 완료"))


class Delete(APIView):
    def post(self, request):
        user_id = request.session.get('user_id', None)
        cover_id = request.data.get('cover_id', "")
        cover = Cover.objects.get(user_id=user_id, id=cover_id)
        if cover:
            cover.delete()

        return Response(status=200, data=dict(msg="삭제 완료"))


class Detail(APIView):
    def post(self, request):
        user_id = request.session.get('user_id', None)
        cover_id = request.data.get('cover_id', None)
        cover = Cover.objects.filter(user_id=user_id, id=cover_id)
        cover_list = []
        for cover in cover:
            cover_list.append(dict(subject=cover.subject,
                                   content=cover.content
                                   ))
        return Response(dict(cover=cover_list))


