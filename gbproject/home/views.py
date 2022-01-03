from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator
from django.http import HttpResponseRedirect
import json
from .result import input_data


def CvService(request):
    if request.method == 'POST':
        user_id = request.session.get('user_id', None)
        print(user_id)
        input1 = request.POST['input1']
        response = input_data(input1)
        #response = (input1)
        output = dict()
        output['response'] = response #여기 저장되는 메시지가 전송됨
        return JsonResponse({'output': response}, status=200)
        
        #input1 = request.POST['input1']
        #response = (input1)
        #input2 = dict()
        #input2['response'] = response
        #print(input2)
        #return JsonResponse({'input2': response}, status=200)
    else:
        return HttpResponse("키워드를 입력해주세요")