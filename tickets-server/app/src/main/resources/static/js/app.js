$(document).ready(
    function() {
        $("#postPerformances").submit(
            function(event) {
                event.preventDefault();
                const data = {
                    name : $("#title").val(),
                    description : $("#description").val(),
                    startDate : $("#startDate").val(),
                    endDate : $("#endDate").val(),
                    price : parseFloat($("#price").val()),
                    address : $("#address").val(),
                    seatsLeft : parseInt($("#seatsLeft").val()),
                }
                postImage().then(postIm => {
                    data.imageURI = postIm
                    console.log(data);
                    uploadPerformance(data);
                })
            });
    });

const postImage = () => {
    const data = new FormData();
    data.append('image', $('#image').prop('files')[0]);
    return new Promise( ((resolve, reject) => $.ajax({
        type : "POST",
        url : "https://secret-hamlet-93924.herokuapp.com/upload",
        data : data,
        cache : false,
        contentType: false,
        processData: false,
        success : function(data, status, request) {
            $("#result").html(
                "<div class='alert alert-success lead'>Success uploading image</div>");
            resolve(`https://secret-hamlet-93924.herokuapp.com/${data.path}`);
        },
        error : function() {
            reject()
        }
    })));
}

const uploadPerformance = async (data) => {
    $.ajax({
        type : "POST",
        url : "/performances",
        data : JSON.stringify(data),
        contentType: "application/json",
        success : function(msg, status, request) {
            $("#result").html(
                "<div class='alert alert-success lead'>Success</div>");
        },
        error : function() {
            $("#result").html(
                "<div class='alert alert-danger lead'>ERROR</div>");
        }
    });
}