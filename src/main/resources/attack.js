const USERNAME_INPUT_XPATH = "//*[@id=\"app\"]/div/div[1]/div[2]/div/div[2]/form/div[1]/div/div[1]/input";

const PASSWORD_INPUT_XPATH = "//*[@id=\"app\"]/div/div[1]/div[2]/div/div[2]/form/div[2]/div/div/input";

const LOGIN_BUTTON_XPATH = "//*[@id=\"app\"]/div/div[1]/div[2]/div/div[2]/button";

function getElementByXpath(xpath){
    let element = document.evaluate(xpath,document).iterateNext();
    return element;
}
let username = getElementByXpath(USERNAME_INPUT_XPATH);
username.value = "118230020411";
let password = getElementByXpath(PASSWORD_INPUT_XPATH);
password.value = "Dr4unpJ3bXfeVm5K";

