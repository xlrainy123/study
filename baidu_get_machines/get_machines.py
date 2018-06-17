import  requests
import  json

# -*- coding: utf-8 -*-

def get_product_info():
    response = requests.get("http://pbu.noah.baidu.com/pbu-service/index.php?r=View/GetAllProdBusiInfo")
    content = json.loads(response.content)
    products = content['data']
    #print(products)
    #print(len(products))
    product_info = []
    for product in products.items():
        curInfo = []
        #print(product)
        curInfo.append(product[0])
        curInfo.append(product[1].get('id'))
        curInfo.append(product[1].get('name'))
        product_info.append(curInfo)
    print(product_info)
    return  product_info


def get_machines_by_nodeid():
    print("------------------------分割线--------------------------")
    product_infos = [['205943403','450','平台治理工具']]
    index = 0
    errorNodeids = []
    info = []
    for product_info in product_infos:
        cur_result = {}
        nodeid = product_info[0]
        product_line_id = product_info[1]
        product_line_name = product_info[2]

        nodeids = {'nodeids': nodeid, 'fields': 'id'}
        machines_response = requests.get("http://noah.baidu.com/goat/index.php?r=Machine/MachineExternal/GetAssetHostByNodeids",
                                        params=nodeids)
        content_byte = machines_response.content
        #print(content_byte)
        try:
            content = json.loads(content_byte)
        except json.decoder.JSONDecodeError:
            error = []
            error.append(nodeid)
            error.append(product_line_id)
            error.append(product_line_name)
            errorNodeids.append(error)
        else:
            machines = content['data']    #list
            machines_count = len(machines)
            cur_result['node_id'] = nodeid
            cur_result['product_line_id'] = product_line_id
            cur_result['product_line_name'] = product_line_name
            cur_result['machines_count'] = machines_count
            info.append(cur_result)
    print(errorNodeids)
    print(info)
    return info

def write2txt(filename, source):
    fp = open(filename, 'w+', encoding='utf-16')
    title = "节点id"+"     "+"产品线id"+"      "+"产品名称"+"       "+"机器数量\n"
    fp.write(title)
    content = '{0},{1},{2},{3}\n'
    for cur_result in source:
        content2 = content.format(cur_result.get('node_id'), cur_result.get('product_line_id'),
                                  cur_result.get('product_line_name'), cur_result.get('machines_count'))
        fp.write(content2)
    fp.close()

#source = get_machines_by_nodeid()
#write2txt("test2_utf_16.csv", source)


#get_product_info()
info = get_machines_by_nodeid()
#print(info)
#write2txt("target.txt", info)