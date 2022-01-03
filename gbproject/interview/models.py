from django.db import models

# Create your models here.
class Interview(models.Model):
    user_id = models.CharField(max_length=50, blank=True, null=True)
    question = models.CharField(verbose_name="인터뷰제목", max_length=256, null=False, default='')
    answer = models.TextField()

    class Meta:
        db_table = 'interview'
        verbose_name = '인터뷰 테이블'