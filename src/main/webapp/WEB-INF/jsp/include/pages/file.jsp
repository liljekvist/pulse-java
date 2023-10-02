<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
        crossorigin="anonymous"></script>
<link href="https://cdn.datatables.net/v/bs5/jq-3.7.0/dt-1.13.6/r-2.5.0/datatables.min.css"
      rel="stylesheet">

<script src="https://cdn.datatables.net/v/bs5/jq-3.7.0/dt-1.13.6/r-2.5.0/datatables.min.js"></script>

<script src="https://cdn.jsdelivr.net/npm/lodash@4.17.21/lodash.min.js"></script>

<style>
  #holder {
    border: 10px dashed #ccc;
    width: 300px;
    height: 300px;
    margin: 20px auto;
  }

  #holder.hover {
    border: 10px dashed #333;
  }
</style>

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">User Management</h1>
    </div>

    <article id="uploadArticle" class="d-grid">
        <i>please drag and drop a file</i>
        <div id="holder"></div>
        <p id="help_text" style="color: red;"></p>
        <p id="status">File API & FileReader API not supported</p>
    </article>
    <article id="userTableArticle" class="d-none">
        <table id="userTable" class="table table-striped" style="width:100%">
            <thead>
            <tr>
                <th>Email</th>
                <th>Firstname</th>
                <th>Lastname</th>
                <th>Phone Number</th>
                <th>Password</th>
                <th>Role</th>
            </tr>
            </thead>
        </table>
        <input type="button" id="submit" name="Submit" value="Submit">
    </article>
</main>
<script>

  $(document).ready(function () {
    const table = new DataTable('#userTable', {
      responsive: true,
      searching: false,

    });

    let file = null,
        file_data = null;

    var json_data = null;

    let holder = document.getElementById('holder'),
        state = document.getElementById('status'),
        help = document.getElementById('help_text'),
        submit = document.getElementById('submit'),
        userTable = document.getElementById('userTableArticle'),
        uploadArticle = document.getElementById('uploadArticle');

    if (typeof window.FileReader === 'undefined') {
      state.className = 'fail';
    } else {
      state.className = 'success';
      state.innerHTML = 'File API & FileReader available';
    }

    holder.ondragover = function () {
      this.className = 'hover';
      return false;
    };
    holder.ondragend = function () {
      this.className = '';
      return false;
    };
    holder.ondrop = function (e) {
      this.className = '';
      e.preventDefault();

      file = e.dataTransfer.files[0];
      let reader = new FileReader();
      reader.onload = function (event) {
        holder.innerText = event.target.result;
        file_data = event.target.result;
        $.ajax({
          url: "/api/admin/file/check",
          type: "POST",
          data: file_data,
          processData: false,
          contentType: false,
          headers: {
            '${_csrf.headerName}': '${_csrf.token}'
          },
          success: function (data) {
            console.log(data);
            json_data = _.cloneDeep(data);
            for (let i in data) {
              table.row.add([
                data[i].email,
                data[i].firstname,
                data[i].lastname,
                data[i].phonenr,
                data[i].password,
                data[i].role.name
              ]).draw();
            }
            userTable.classList.remove("d-none");
            userTable.classList.add("d-grid");
            uploadArticle.classList.remove("d-grid");
            uploadArticle.classList.add("d-none");
          },
          error: function (data) {
            help.innerText = data.responseText;
          }
        });
      };
      console.log(file);
      reader.readAsText(file);

      return false;
    };

    submit.onclick = function () {
      if (file_data == null) {
        alert("please drag and drop a file");
        return;
      }
      $.ajax({
        url: "/api/admin/file/upload",
        type: "POST",
        data: JSON.stringify(json_data),
        contentType: "application/json",
        processData: false,
        headers: {
          '${_csrf.headerName}': '${_csrf.token}'
        },
        success: function (data) {
          window.location.href = "/admin/users";
        }
      });
    }
  });


</script>

