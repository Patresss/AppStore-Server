from flask import Flask, render_template, request, abort, redirect, url_for
from flask import make_response
import requests
import json
import base64
app = Flask(__name__)

SERV_ADDR = 'http://192.168.43.63:8080/'


@app.route('/')
def get_list():
    headers = {'Content-type': 'application/json','Accept': '*/*'}
    r = requests.get("%sapi/games" % SERV_ADDR,headers=headers)
    return render_template("home.html", games=json.loads(r.text), admin=request.cookies.get('admin', 'False'))


@app.route('/details/<int:id>')
def details(id):
    headers = {'Content-type': 'application/json','Accept': '*/*'}
    r = requests.get("%sapi/games/%s" % (SERV_ADDR,id), headers=headers)
    if r.status_code != 200:
        abort(404)
    return render_template('details.html', game=json.loads(r.text))


@app.route('/admin', methods=['GET', 'POST'])
def admin():
    admin = request.cookies.get('admin', 'False')
    if admin != "True":
        return redirect(url_for('get_list'))

    errors = {}
    if request.method == 'POST':
        gname = request.form.get('gname', None)
        if gname == "":
            errors['gname'] = "Name can not be empty"
        gversion = request.form.get('gversion', None)
        if gversion == "":
            errors['gversion'] = "Version can not be empty"
        gdescription = request.form.get('gdescription', None)
        if gdescription == "":
            errors['gdescription'] = "Description can not be empty"
        gimage = request.files['gimage'].read()
        if gimage != "":
            gimage = base64.b64encode(gimage).decode('utf-8')
        gicon = request.files['gicon'].read()
        if gicon!= "":
            gicon = base64.b64encode(gicon).decode('utf-8')
        gfile = request.files['gfile'].read()
        if gfile!= "":
            gfile = base64.b64encode(gfile).decode('utf-8')

        if len(errors) == 0:

            data = {
                "description": "%s" % gdescription,
                "name": "%s" % gname,
                "version": "%s" % gversion,
            }
            if gimage != None:
                data.update({"image": "%s" % gimage,})
            if gicon != None:
                data.update({'icon': "%s" % gicon,})
            if gfile != None:
                data.update({'file': "%s" % gfile,})

            print(data)
            headers = {'Content-type': 'application/json','Accept': 'text/plain'}
            r = requests.post("%sapi/games" % SERV_ADDR,data=json.dumps(data),headers=headers)

            if r.status_code == 406:
                return redirect(url_for('get_list'))

    return render_template('admin.html', errors=errors, values=request.form)


@app.route('/messanger')
def get_messanger():
    return render_template('messanger.html')


@app.route('/delete/<int:id>', methods=['GET', 'POST'])
def delete(id):
    admin = request.cookies.get('admin', 'False')
    if admin != "True":
        return redirect(url_for('get_list'))

    headers = {'Content-type': 'application/json','Accept': '*/*'}
    r = requests.get("%sapi/games/%s" % (SERV_ADDR, id), headers=headers)
    if r.status_code != 200:
        abort(404)

    if request.method == "POST":
        id = request.form.get("game_id", 0)
        headers = {'Content-type': 'application/json','Accept': 'text/plain'}
        r = requests.delete("%sapi/games/%s" % (SERV_ADDR, id), headers=headers)

        if r.status_code == 200:
            return redirect(url_for('get_list'))

    return render_template('delete.html', game=json.loads(r.text))


@app.route('/edit/<int:id>', methods=['GET', 'POST'])
def edit(id):
    admin = request.cookies.get('admin', 'False')
    if admin != "True":
        return redirect(url_for('get_list'))

    errors = {}

    headers = {'Content-type': 'application/json','Accept': '*/*'}
    r = requests.get("%sapi/games/%s" % (SERV_ADDR, id), headers=headers)
    if r.status_code != 200:
        abort(404)

    if request.method == 'POST':
        gname = request.form.get('gname', None)
        if gname == "":
            errors['gname'] = "Name can not be empty"
        gversion = request.form.get('gversion', None)
        if gversion == "":
            errors['gversion'] = "Version can not be empty"
        gdescription = request.form.get('gdescription', None)
        if gdescription == "":
            errors['gdescription'] = "Description can not be empty"
        gid = request.form.get("gid", 0)
        gimage = request.form.get('ghimage', "")
        if gimage == "":
            gimage = request.files['gimage'].read()
            if gimage != "":
                gimage = base64.b64encode(gimage).decode('utf-8')
        gicon = request.form.get('ghicon', "")
        if gicon == "":
            gicon = request.files['gicon'].read()
            if gicon!= "":
                gicon = base64.b64encode(gicon).decode('utf-8')
        gfile = request.form.get('ghfile', "")
        if gfile == "":
            gfile = request.files['gfile'].read()
            if gfile!= "":
                gfile = base64.b64encode(gfile).decode('utf-8')

        if len(errors) == 0:

            data = {
                "id" : "%s" % gid,
                "description": "%s" % gdescription,
                "name": "%s" % gname,
                "version": "%s" % gversion,
            }
            if gimage != None:
                data.update({'image': "%s" % gimage,})
            if gicon != None:
                data.update({'icon': "%s" % gicon,})
            if gfile != None:
                data.update({'file': "%s" % gfile,})
            headers = {'Content-type': 'application/json','Accept': 'text/plain'}
            r = requests.put("%sapi/games" % SERV_ADDR, data=json.dumps(data),headers=headers)

            if r.status_code == 406:
                return redirect(url_for('get_list'))

    return render_template('edit.html', values=json.loads(r.text), errors=errors )


@app.route('/login', methods=['GET', 'POST'])
def login_admin():
    admin = request.cookies.get('admin', 'False')
    if admin == "True":
        return redirect(url_for("get_list"))

    errors = {}
    if request.method == 'POST':
        login = request.form.get("login", None)
        password = request.form.get("password", None)

        if login == "admin" and password == "admin":
            response = make_response(redirect(url_for('get_list')))
            response.set_cookie("admin", "True")
            return response
        else:
            errors['error'] = "Wrong login or password"

    return render_template('login.html', errors=errors)


@app.route('/logout', methods=['GET'])
def logout():
    response = make_response(redirect(url_for('get_list')))
    admin = request.cookies.get('admin', 'False')
    if admin == "True":
        response.set_cookie("admin", expires=0)

    return response


@app.errorhandler(404)
def page_not_found(e):
    return render_template('404.html'), 404
