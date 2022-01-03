from django.conf.urls import url
from . import views

urlpatterns = [
    url('create', views.Create.as_view(), name='create'),
    url('delete', views.Delete.as_view(), name='delete'),
    url('select', views.Select.as_view(), name='select'),
    url('update', views.Update.as_view(), name='update'),
    url('detail', views.Detail.as_view(), name='detail'),

]