from __future__ import unicode_literals
from threading import Timer
from wxpy import *
import requests
import  json

bot = Bot()

def get_news():
    """获取金山词霸每日一句，英文和翻译"""
    url = "http://open.iciba.com/dsapi"
    r = requests.get(url)
    contents = r.content
    contents = json.loads(contents)
    content = contents['content']
    note = contents['note']
    return content, note
cnt = 1
def send_news():
    global cnt
    try:
        contents = get_news()
        print(contents[0], '\n', contents[1], '\n')
        my_friend = bot.friends().search(u'柯西')[0]
       # my_friend.send(contents[0])
        my_friend.send(contents[1])
        my_friend.send(cnt)
        cnt = cnt+1
       # my_friend.send(u"have a good one!")
        t = Timer(2, send_news)
        t.start()
    except:
        my_friend = bot.friends().search(u'Shannon')[0]
        my_friend.send(u'今天消息发送失败')

if __name__ == "__main__":
    send_news()