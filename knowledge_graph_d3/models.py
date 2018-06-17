# -*- coding: utf-8 -*-
import pymysql
import json
import os

edge_sql_comp = """SELECT subj, obj, pred, company.company_name, company.code, person.name, type FROM spo JOIN company JOIN person WHERE spo.subj=company.id AND spo.obj=person.id AND company.code=%s;"""
edge_sql_pers = """SELECT subj, obj, pred, company.company_name, company.code, person.name, type FROM spo JOIN company JOIN person WHERE spo.subj=company.id AND spo.obj=person.id AND person.name="%s";"""
secondary_edge_sql = 'SELECT * FROM spo WHERE subj="%s"'

fname = os.getcwd() + "/templates/data.json"

def init():
	conn = pymysql.connect(
			host = 'localhost',
			port = 3306,
			user = 'root',
			password = '',
			charset ='utf8',
			db = 'knowledge_graph')
	cursor = conn.cursor()
	return conn, cursor

def execute(conn, cursor, attr):
    js = {}
    edges, secondary_nodes, secondary_edges = [], [], []
    try:
        sql = edge_sql_comp%(attr[1]) if attr[0]=='company' else edge_sql_pers%(attr[1])
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            if row[-1]=="relation":
                secondary_nodes.append((row[1], row[5]))  # u"高管"  row[3]
                edges.append({"source": "xlcheng", "target": row[5], "relation": row[2], "label": row[-1]})
            # else:
                # secondary_edges.append({"source": row[3], "target": row[1], "relation": row[2], "label": row[-1]})
        for node in secondary_nodes:
            sql = secondary_edge_sql % node[0]
            print(sql)
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                if row[1]==u'姓名': continue
                secondary_edges.append({"source": node[1], "target": row[2], "relation": row[1], "label": row[3]})
    except:
        conn.rollback()
    # js["nodes"] = nodes
   # print(secondary_edges)
    js["edges"] = edges
    js["secondary_edges"] = secondary_edges
    mydata = json.dumps(js, ensure_ascii=False).encode("utf8")
    with open(fname, 'wb+') as f:
        f.write(mydata)
        f.close()
    return mydata
