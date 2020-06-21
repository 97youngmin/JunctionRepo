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
RESTAURANT = "restaurant"

## init
app = Flask(__name__)
table_service = TableService(account_name='xjunction', account_key='/NVe744lwsYU8tPKp5+AjqTyUuBRkSxG5o0pAQfA+3tggJ0DdEaKRwEx9M+mmrbOqc8B2bAljyPfFW4snl/zLA==')
geolocator = Nominatim()


def create_table():
    # Create a new table
    table_name = 'Uploads'
    print('Create a table with name - ' + table_name)
    table_service.create_table(table_name)
    print('done')

def delete_table():
    table_service.delete_table('Uploads')

def insert_supplier(name, location, category, phone, closing):
    ## can insert or update supplier_dashboard_fragment
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
    ## can insert or update supplier_dashboard_fragment
    print('Creating User')
    task = Entity()
    task.PartitionKey = email
    task.RowKey = name
    task.penalty = penalty
    task.following = following
    table_service.insert_or_replace_entity(USER_TABLE, task)
    print('Created in User: {name} with uuid {uuid}'.format(email=email, name=name))

def insert_uploads(food, expiry, image, price, quantity, company):
    ## can insert or update supplier_dashboard_fragment
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
#     task = table_service.get_entity('Test', name, supplier_dashboard_fragment[name])
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
    if not request.json:
        abort(400)
    #print(request.files['image'])
    #f = request.files['image']
    #f.save(os.path.join(app.root_path, 'image', secure_filename(f.filename)))
    #req = request.form.to_dict()
    req = request.json
    print(request.json)
    id_upload = insert_uploads(req["name"], req["expiry"], req["image"], req["price"], req["quantity"], req["company"])
    return jsonify({'success': 'true', 'id': id_upload}), 200


@app.route('/food', methods=['POST'])
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
        if request.json['category'] == "" or request.json['category'] == supplier['category']:
            supplier.pop('name', None)
            temp.update(supplier)
            result.append(temp)

    result.sort(key=lambda d: sqrt((abs(lat - d["lat"]) ** 2) + (abs(lon - d["long"]) ** 2)))
    print(result)
    return jsonify(result), 200


@app.route('/')
def populate():
    # insert_supplier("FairPrice", "587 Bukit Timah Rd, Singapore 269707", "supermarket", "+6564696245", "23:00")
    # insert_supplier("Giant", "144 Upper Bukit Timah Rd, 588177", "supermarket", "+6564637804", "-")
    # insert_supplier("Crown Bakery & Cafe", "557 Bukit Timah Road, 269694", "bakery", "+6564633066", "18:30")
    # insert_supplier("The Bread Table", "145 Upper Bukit Timah Rd", "bakery", "+6565533066", "16:30")
    # insert_supplier("Adam Road Food Centre", "2 Adam Rd, 289877", "food centre", "+6562255632", "00:00")
    # insert_supplier("Al-Azhar Restaurant", "11 Cheong Chin Nam Rd, 599736", "restaurant", "+6564665052", "-")
    # insert_uploads("Cake", "21/06/2020", "https://bakingamoment.com/wp-content/uploads/2017/06/IMG_3437-tiramisu-cake-square.jpg", 1.50, 3, "Crown Bakery & Cafe")
    # insert_uploads("Bread", "21/06/2020", "https://images.kitchenstories.io/recipeImages/34_02_RusticGermanBread_TitlePicture/34_02_RusticGermanBread_TitlePicture-medium-landscape-150.jpg", 1.50, 3, "The Bread Table")
    # insert_uploads("Beef", "21/06/2020", "https://www.bbcgoodfood.com/sites/default/files/recipe-collections/collection-image/2013/05/roast-beef-recipes.jpg", 2.70, 4  , "FairPrice")
    # insert_uploads("Eggs", "21/06/2020", "https://img3.exportersindia.com/product_images/bc-full/dir_71/2113329/deshi-eggs-2004562.jpg", 1.20, 10  , "FairPrice")
    # insert_uploads("Tuna Can", "25/06/2020", "https://aw6w1k9h0c-flywheel.netdna-ssl.com/wp-content/uploads/2019/02/Albacore_Angle_IMG_2669.jpg", 1.80, 18, "Giant")
    # insert_uploads("Bread", "21/06/2020", "https://burpple-1.imgix.net/foods/25e09a4277ab67571a883556_original.?w=400&h=400&fit=crop&q=80&auto=format", 0.30, 6, "Giant")
    # insert_uploads("Rice", "21/06/2020", "https://cdn.loveandlemons.com/wp-content/uploads/2020/03/rice.jpg", 0.10, 7, "Al-Azhar Restaurant")
    # insert_uploads("Butter Chicken", "21/06/2020", "https://healthyfitnessmeals.com/wp-content/uploads/2020/01/Butter-chicken.jpg", 5.30, 4, "Al-Azhar Restaurant")
    # insert_uploads("Veggies", "21/06/2020", "https://s.yimg.com/ny/api/res/1.2/2TkIIfQ95EMfykMWt.lWFA--~A/YXBwaWQ9aGlnaGxhbmRlcjtzbT0xO3c9ODAw/http://media.zenfs.com/en-SG/homerun/sethlui/c81a7fa1b9397b6d6495b0114fb951fe", 0.20, 3, "Adam Road Food Centre")
    #
    # insert_uploads("Satay", "21/06/2020", "https://www.rotinrice.com/wp-content/uploads/2013/09/IMG_9567.jpg", 0.10, 30, "Adam Road Food Centre")
    # query_all_table(UPLOADS_TABLE)
    # delete_table()
    # create_table()
    return "Hello World!"

if __name__ == '__main__':
    app.run()