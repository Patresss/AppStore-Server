from flask import Flask, render_template, request, abort, redirect, url_for
import requests
import json
import base64
app = Flask(__name__)


@app.route('/')
def get_list():
    headers = {'Content-type': 'application/json','Accept': '*/*'}
    r = requests.get("http://localhost:8080/api/games",headers=headers)
    return render_template("home.html", games=json.loads(r.text))


@app.route('/details/<int:id>')
def get_details(id):
    headers = {'Content-type': 'application/json','Accept': '*/*'}
    r = requests.get("http://localhost:8080/api/games/%s" % id, headers=headers)
    if r.status_code != 200:
        abort(404)
    return render_template('details.html', game=json.loads(r.text))


@app.route('/admin', methods=['GET', 'POST'])
def get_admin():
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

        if len(errors) == 0:

            baseEncodedImage = "%s" % base64.b64encode(request.files['gimage'].read())
            if len(baseEncodedImage) >= 3:
                baseEncodedImage = baseEncodedImage[2:-1]

            baseEncodedIcon = "%s" % base64.b64encode(request.files['gicon'].read())
            if len(baseEncodedImage) >= 3:
                baseEncodedIcon = baseEncodedIcon[2:-1]
            data = {
                "description": "%s" % gdescription,
                "image": baseEncodedImage,
                "icon": baseEncodedIcon,
                "name": "%s" % gname,
                "version": "%s" % gversion,
            }
            headers = {'Content-type': 'application/json','Accept': 'text/plain'}
            r = requests.post("http://localhost:8080/api/games",data=json.dumps(data),headers=headers)

            if r.status_code == 406:
                return redirect(url_for('get_list'))

    return render_template('admin.html', errors=errors, values=request.form)


@app.route('/messanger')
def get_messanger():
    return render_template('messanger.html')


@app.route('/delete/<int:id>', methods=['GET', 'POST'])
def delete(id):
    headers = {'Content-type': 'application/json','Accept': '*/*'}
    r = requests.get("http://localhost:8080/api/games/%s" % id, headers=headers)
    if r.status_code != 200:
        abort(404)

    if request.method == "POST":
        id = request.form.get("game_id", 0)
        headers = {'Content-type': 'application/json','Accept': 'text/plain'}
        r = requests.delete("http://localhost:8080/api/games/%s" % id, headers=headers)

        if r.status_code == 200:
            return redirect(url_for('get_list'))

    return render_template('delete.html', game=json.loads(r.text))


@app.route('/edit/<int:id>', methods=['GET', 'POST'])
def edit(id):
    errors = {}

    headers = {'Content-type': 'application/json','Accept': '*/*'}
    r = requests.get("http://localhost:8080/api/games/%s" % id, headers=headers)
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

        if len(errors) == 0:

            baseEncodedImage = "%s" % base64.b64encode(request.files['gimage'].read())
            if len(baseEncodedImage) >= 3:
                baseEncodedImage = baseEncodedImage[2:-1]

            baseEncodedIcon = "%s" % base64.b64encode(request.files['gicon'].read())
            if len(baseEncodedImage) >= 3:
                baseEncodedIcon = baseEncodedIcon[2:-1]

            data = {
                "id" : "%s" % gid,
                "description": "%s" % gdescription,
                "image": baseEncodedImage,
                "icon": baseEncodedIcon,
                "name": "%s" % gname,
                "version": "%s" % gversion,
            }
            headers = {'Content-type': 'application/json','Accept': 'text/plain'}
            r = requests.put("http://localhost:8080/api/games", data=json.dumps(data),headers=headers)

            if r.status_code == 406:
                return redirect(url_for('get_list'))

    return render_template('edit.html', values=json.loads(r.text), errors=errors )


@app.errorhandler(404)
def page_not_found(e):
    return render_template('404.html'), 404
