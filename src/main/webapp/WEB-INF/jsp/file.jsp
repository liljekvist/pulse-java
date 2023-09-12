<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>file test</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    <script src="https://malsup.github.io/jquery.form.js"></script>

    <style>
        #holder { border: 10px dashed #ccc; width: 300px; height: 300px; margin: 20px auto;}
        #holder.hover { border: 10px dashed #333; }
    </style>

</head>
<body>
<article>
    <i>please drag and drop a file</i>
    <div id="holder"></div>
    <p id="help_text" style="color: red;"></p>
    <p id="status">File API & FileReader API not supported</p>

    <input type="button" id="submit" name="Submit" value="Submit">
</article>
</body>
</html>
<script>
    let file = null,
        file_data = null;
    // modified from http://html5demos.com/file-api
    var holder = document.getElementById('holder'),
        state = document.getElementById('status'),
        help = document.getElementById('help_text'),
        submit = document.getElementById('submit');

    if (typeof window.FileReader === 'undefined') {
        state.className = 'fail';
    } else {
        state.className = 'success';
        state.innerHTML = 'File API & FileReader available';
    }

    holder.ondragover = function() {
        this.className = 'hover';
        return false;
    };
    holder.ondragend = function() {
        this.className = '';
        return false;
    };
    holder.ondrop = function(e) {
        this.className = '';
        e.preventDefault();

        file = e.dataTransfer.files[0];
        let reader = new FileReader();
        reader.onload = function(event) {
            holder.innerText = event.target.result;
            file_data = event.target.result;
            $.ajax({
                url: "/api/file/check",
                type: "POST",
                data: file_data,
                processData: false,
                contentType: false,
                success: function (data) {
                    help.innerText = "";
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
        if(file_data == null) {
            alert("please drag and drop a file");
            return;
        }
        $.ajax({
            url: "/api/file/upload",
            type: "POST",
            data: file_data,
            processData: false,
            contentType: false,
            success: function (data) {
                console.log(data);
            }
        });
    }


</script>

