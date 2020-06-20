from flask import Flask, jsonify, abort, request
from azure.cosmosdb.table import TableService
from azure.cosmosdb.table import Entity
import uuid 
from werkzeug.utils import secure_filename
import os
from  geopy.geocoders import Nominatim
import json
from math import sqrt


## constants
USER_TABLE = "User"
SUPPLIER_TABLE = "Supplier"
UPLOADS_TABLE = "Uploads"

SUPERMARKET = "supermarket"
BAKERY = "bakery"
HAWKER = "food centre"

## init
app = Flask(__name__)
table_service = TableService(account_name='xjunction', account_key='/NVe744lwsYU8tPKp5+AjqTyUuBRkSxG5o0pAQfA+3tggJ0DdEaKRwEx9M+mmrbOqc8B2bAljyPfFW4snl/zLA==')
geolocator = Nominatim()


def create_table():
    # Create a new table
    table_name = 'Supplier'
    print('Create a table with name - ' + table_name)
    table_service.create_table(table_name)
    print('done')

def delete_table():
    table_service.delete_table('Supplier')

def insert_supplier(name, location, category, phone, closing):
    ## can insert or update supplier
    print('Creating Supplier')
    id_supplier = str(uuid.uuid1())
    task = Entity()
    task.PartitionKey = name
    task.RowKey = id_supplier
    task.location = location
    print(location)
    location_data = geolocator.geocode(location)
    task.lat = location_data.latitude
    task.long = location_data.longitude
    task.category = category
    task.phone = phone
    task.closing = closing
    table_service.insert_or_replace_entity(SUPPLIER_TABLE, task)
    print('Created in Supplier: {name} with uuid {uuid}'.format(name=name, uuid=id_supplier))

def insert_user(email, name, penalty, following):
    ## can insert or update supplier
    print('Creating User')
    task = Entity()
    task.PartitionKey = email
    task.RowKey = name
    task.penalty = penalty
    task.following = following
    table_service.insert_or_replace_entity(USER_TABLE, task)
    print('Created in User: {name} with uuid {uuid}'.format(email=email, name=name))

def insert_uploads(food, expiry, image, price, quantity, company):
    ## can insert or update supplier
    print('Creating Uploads')
    id_upload = str(uuid.uuid1())
    task = Entity()
    task.PartitionKey = food
    task.RowKey = id_upload
    task.expiry = expiry
    task.image = image
    task.price = price
    task.quantity = quantity
    task.company = company
    table_service.insert_or_replace_entity(UPLOADS_TABLE, task)
    print('Created in Uploads: {name} with uuid {uuid}'.format(name=food, uuid=id_upload))
    return id_upload

# def get_supplier(name):
#     task = table_service.get_entity('Test', name, supplier[name])
#     print(task.phone)

def to_json(table, task):
    if table == SUPPLIER_TABLE:
        return {
            'name': task.PartitionKey,
            'id': task.RowKey,
            'location': task.location,
            'lat': task.lat,
            'long': task.long,
            'category': task.category,
            'phone': task.phone,
            'closing': task.closing,
        }
    elif table == UPLOADS_TABLE:
        return {
            'name': task.PartitionKey,
            'id': task.RowKey,
            'expiry': task.expiry,
            'image': task.image,
            'price': task.price,
            'quantity': task.quantity,
            'company': task.company,
        }
    elif table == USER_TABLE:
        return {
            'email': task.PartitionKey,
            'name': task.RowKey,
            'penalty': task.penalty,
            'following': task.following,
        }
    return None

def query(table, name):
    tasks = table_service.query_entities(table, filter="PartitionKey eq '{}'".format(name))
    result = []
    for task in tasks:
        result.append(to_json(table, task))
    return result

def query_category(name, category):
    tasks = table_service.query_entities(SUPPLIER_TABLE, filter="PartitionKey eq '{}'".format(name))
    result = []
    for task in tasks:
        if not task.category is category:
            pass;
        result.append(to_json(SUPPLIER_TABLE, task))
    return result

def query_all(table):
    tasks = table_service.query_entities(table)
    result = []
    for task in tasks:
        result.append(to_json(table, task))
    return result


@app.route('/upload', methods=['POST'])
def upload():
    if not request.form:
        abort(400)
    print(request.files['image'])
    f = request.files['image']
    f.save(os.path.join(app.root_path, 'image', secure_filename(f.filename)))
    print(request.form.to_dict())
    req = request.form.to_dict()
    id_upload = insert_uploads(req["name"], req["expiry"], f.filename, req["price"], req["quantity"])
    return jsonify({'success': 'true', 'id': id_upload}), 200


@app.route('/food', methods=['GET'])
def get_food():
    if not request.json:
        abort(400)
    print(request.json)
    address = request.json['location']
    current_location = geolocator.geocode(address)
    lat = current_location.latitude
    lon = current_location.longitude
    result = []
    res = query_all(UPLOADS_TABLE)
    for data in res:
        temp = data
        supplier = query(SUPPLIER_TABLE, data.get("company"))[0]
        if not request.args.get('category') is None:
            if request.args.get('category') == supplier['category']:
                supplier.pop('name', None)
                temp.update(supplier)
                result.append(temp)
        else: 
            supplier.pop('name', None)
            temp.update(supplier)
            result.append(temp)
    result.sort(key=lambda d: sqrt((abs(lat - d["lat"]) ** 2) + (abs(lon - d["long"]) ** 2)))
    print(result)
    return jsonify(result), 200


@app.route('/')
def populate():
    # insert_supplier("FairPrice", "587 Bukit Timah Rd, Singapore 269707", "supermarket", "+6564696245", "23:00")
    # insert_supplier("Crown Bakery & Cafe", "557 Bukit Timah Road, 269694", "bakery", "+6564633066", "18:30")
    # insert_supplier("Adam Road Food Centre", "2 Adam Rd, 289877", "food centre", "+6562255632", "00:00")
    # insert_uploads("Bread", "20/06/2020", "/hello.jpg", 0.50, 5, "FairPrice")
    # insert_uploads("tuna", "20/07/2020", "/hello.jpg", 1.50, 10, "FairPrice")
    # insert_uploads("Bread", "21/06/2020", "/hello.jpg", 0.80, 3, "Crown Bakery & Cafe")
    # insert_uploads("karage", "20/06/2020", "/hello.jpg", 0.30, 3, "Adam Road Food Centre")
    # query_all_table(UPLOADS_TABLE)
    # delete_table()
    # create_table()
    return "Hello World!"

if __name__ == '__main__':
    app.run()