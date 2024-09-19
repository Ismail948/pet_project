document.addEventListener('DOMContentLoaded', function () {
    const baseUrl = window.location.origin;
    document.getElementById('home-link').href = `${baseUrl}/`;
    document.getElementById('explore-link').href = `${baseUrl}/explore`;
    document.getElementById('shopping-link').href = `${baseUrl}/shopping`;
    document.getElementById('hospitals-link').href = `${baseUrl}/hospitals`;
    document.getElementById('contact-link').href = `${baseUrl}/contact`;

    const loginButton = document.getElementById('login-signup-btn');
    if (loginButton) {
        loginButton.addEventListener('click', function () {
            window.location.href = `${baseUrl}/login`;
        });
    }

    // Dropdown click toggle for mobile users
    const userWelcome = document.querySelector('.user-welcome');
    const dropdownContent = document.querySelector('.dropdown-content');

    if (userWelcome) {
        userWelcome.addEventListener('click', function () {
            dropdownContent.classList.toggle('show');
        });
    }
});
