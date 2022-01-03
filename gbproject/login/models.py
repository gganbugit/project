from django.db import models
from django.contrib.auth.base_user import AbstractBaseUser

# Create your models here.

class User(AbstractBaseUser):
    user_id = models.CharField(max_length=50, unique=True, null=False, default=False)
    #비밀번호는 생김 상속받아서 사용하기 때문에 실질적으로 우리가 만들어야할 부분 유저 아이디

    USERNAME_FIELD = 'user_id'

    def __str__(self):
        return self.user_id

    @property
    def is_staff(self):
        return self.is_admin

    class Meta:
        db_table = 'users'