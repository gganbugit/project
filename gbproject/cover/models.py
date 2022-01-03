from django.db import models
from login.models import User


class Cover(models.Model):
    user_id = models.CharField(max_length=50, blank=True, null=True)
    subject = models.CharField(verbose_name="자소서주제", max_length=256, null=False, default='')
    content = models.TextField()

    class Meta:
        db_table = 'cover'
        verbose_name = '자소서 테이블'


