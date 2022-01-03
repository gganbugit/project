from django.shortcuts import render
from rest_framework.views import APIView
from .models import Interview
from cover.models import Cover
from rest_framework.response import Response

# class RamdomSubject(APIView):
#     def post(self, request):
#         questionnaire = Interview.objects.get



class Create(APIView):
    def post(self, request):
        user_id = request.session.get('user_id', None)
        interview_id = request.data.get('interview_id', None)  
        question = request.data.get('question', "")
        answer = request.data.get('answer', "")

        if interview_id:
            interview = Interview.objects.create(id=interview_id, user_id=user_id, question=question, answer=answer)
        else:
            interview = Interview.objects.create(user_id=user_id, question=question, answer=answer)

        return Response(data=dict(interview_id=interview.id))


class Select(APIView):
    def get(self, request):
        user_id = request.session.get('user_id', None)
        interviews = Interview.objects.filter(user_id=user_id)
        covers = Cover.objects.filter(user_id=user_id)
        cover_count = covers.count()
        interview_list = []
        interview_count = interviews.count()
        for interview in interviews:
            interview_list.append(dict(user_id=user_id,
                                       cover_count=cover_count,
                                       interview_count=interview_count,
                                       interview_id=interview.id,
                                       question=interview.question,
                                       answer=interview.answer
                                       ))
        interview_list = sorted(interview_list, key=(lambda x: x['interview_id']), reverse=True)
        if len(interview_list) == 0:
            interview_list.append(dict(user_id=user_id,
                                       interview_count=0,
                                       cover_count=cover_count,
                                       interview_id=0,
                                       subject=0,
                                       content=0
                                       ))
            return Response(dict(interviews=interview_list))
        else:
            return Response(dict(interviews=interview_list))


class Update(APIView):
    def post(self, request):
        user_id = request.session.get('user_id', None)
        interview_id = request.data.get('interview_id', "")
        interview = Interview.objects.get(user_id=user_id, id=interview_id)
        interview.question = request.data.get('question', "")
        interview.answer = request.data.get('answer', "")
        interview.save()
        return Response(status=200, data=dict(msg="수정 완료"))


class Delete(APIView):
    def post(self, request):
        user_id = request.session.get('user_id', None)
        interview_id = request.data.get('interview_id', "")
        interview = Interview.objects.get(user_id=user_id, id=interview_id)
        if interview:
            interview.delete()

        return Response(status=200, data=dict(msg="삭제 완료"))


class Detail(APIView):
    def post(self, request):
        user_id = request.session.get('user_id', None)
        interview_id = request.data.get('interview_id', None)
        interview = Interview.objects.filter(user_id=user_id, id=interview_id)
        interview_list = []
        for interview in interview:
            interview_list.append(dict(question=interview.question,
                                       answer=interview.answer
                                       ))
        return Response(dict(interview=interview_list))
