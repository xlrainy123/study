# -*- coding: utf-8 -*-
import pymysql
import json
import os

edge_sql_relation = """select source_user_id,target_user_id,trust_value,interaction_num from relationship where source_user_id=%s;"""
edge_sql_user = """select nikename,sex,zone,follower,interaction_num,education,birth from user where id=%s;"""
fname = os.getcwd() + "/templates/user_data.json"


def init():
	conn = pymysql.connect(
			host = 'localhost',
			port = 3306,
			user = 'root',
			password = '',
			charset ='utf8',
			db = 'online_social_networks')
	cursor = conn.cursor()
	return conn, cursor

def execute(conn, cursor, attr):
    js = {}
    edges, secondary_nodes, secondary_edges = [], [], []
    try:
        sql = edge_sql_relation%attr
        print("sql :", sql)
        cursor.execute(sql)
        results = cursor.fetchall()
        print(len(results))
        for row in results:
            #print("好友：", row[1])
            secondary_nodes.append(row[1])
            sql1 = 'select nikename from user where id=%s'%row[1]
            cursor.execute(sql1)
            nikename = cursor.fetchall()[0][0]
            #edges.append({"source": "xlcheng", "target": row[5], "relation": row[2], "label": row[-1]})
            edges.append({"source": row[0], "target": row[1], "relation": "%s,%s"%(row[2],row[-1])})
        print("secondary_nodes:", secondary_nodes)
        for node in secondary_nodes:
            sql = edge_sql_user%node
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                #secondary_edges.append({"source": node[1], "target": row[2], "relation": row[1], "label": row[3]})
                print("node:",node,"target:",row[0])
                secondary_edges.append({"source": node, "target":row[0], "relation": "昵称"})
                secondary_edges.append({"source": node, "target": row[1], "relation": "性别"})
                secondary_edges.append({"source": node, "target": row[2], "relation": "地区"})
                secondary_edges.append({"source": node, "target": row[3], "relation": "关注者人数"})
                secondary_edges.append({"source": node, "target": row[4], "relation": "交互总次数"})
                secondary_edges.append({"source": node, "target": row[5], "relation": "学历"})
                secondary_edges.append({"source": node, "target": row[6], "relation": "生日"})
        print("secondary_edges", secondary_edges)
    except:
        conn.rollback()
    js["edges"] = edges
    js["secondary_edges"] = secondary_edges
    mydata = json.dumps(js, ensure_ascii=False).encode("utf8")
    with open(fname, 'wb+') as f:
        f.write(mydata)
        f.close()
    return mydata