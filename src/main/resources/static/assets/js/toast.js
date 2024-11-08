const notyf = new Notyf({
    duration: 2000,
    position: {
        x: 'right',
        y: 'top',
    },
    dismissible: true
});

function toast(type, message) {
    notyf.open({
        type: type,
        message: message
    });
}