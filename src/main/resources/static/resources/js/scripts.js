// At Sign Up - Makes sure the fields are correctly fulfilled.
function validateSignupForm() {
    let pass = document.getElementById("password");
    let passRep = document.getElementById("password-repeat");
    let correctPass = false;

    let selectMenu = document.getElementById("selectMenu");
    let roleOption = selectMenu.options[selectMenu.selectedIndex].text;
    let correctOption = false;

    if (pass.value !== passRep.value) {
        pass.style.border = "2px solid red";
        passRep.style.border = "2px solid red";
        document.getElementById("error-message").innerText = "Passwords don't match. ";
    }
    else {
        pass.style.border = "";
        passRep.style.border = "";
        document.getElementById("error-message").innerText = "";
        correctPass = true;
    }

    if (roleOption === 'Choose your role') {
        selectMenu.style.border = "2px solid red";
    }
    else {
        selectMenu.style.border = "";
        correctOption = true;
    }

    if (correctPass && correctOption) {
        pass.style.border = "";
        passRep.style.border = "";
        selectMenu.style.border = "";
        document.getElementsByTagName('form')[0].submit();
    }
}

// Makes the navbar fixed when scroll
document.addEventListener("DOMContentLoaded", function(){
    window.addEventListener('scroll', function() {
        if (window.scrollY > 0) {
            document.getElementById('navbar_top').classList.add('fixed-top');
            // add padding top to show content behind navbar
            let navbar_height = document.querySelector('.navbar').offsetHeight;
            document.body.style.paddingTop = navbar_height + 'px';
        } else {
            document.getElementById('navbar_top').classList.remove('fixed-top');
            // remove padding top from body
            document.body.style.paddingTop = '0';
        }
    });
});

// Password update
function checkPasswordValidity() {
    let pass = document.getElementById("new_password");
    let passConf = document.getElementById("confirm_new_password");

    if (pass.value !== passConf.value) {
        pass.style.border = "2px solid red";
        passConf.style.border = "2px solid red";
        document.getElementById("error-message").innerText = "Passwords don't match. ";
    }
    else {
        pass.style.border = "";
        passConf.style.border = "";
        document.getElementById("error-message").innerText = "";
        document.getElementsByTagName('form')[0].submit();
    }
}

var loadFile = function(event) {
    var image = document.getElementById('output');
    image.src = URL.createObjectURL(event.target.files[0]);
};