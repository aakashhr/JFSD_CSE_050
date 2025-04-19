# Required installations:
# pip install flask python-dotemail

from flask import Flask, render_template, request, redirect, url_for
import smtplib
import random
from email.message import EmailMessage
import os

app = Flask(__name__)
otp_storage = {}  # Temporary storage for OTPs (use database in production)

# Email configuration (use environment variables in production)
EMAIL_ADDRESS = "jalgara.hr@gmail.com"
EMAIL_PASSWORD = "tkhb hucw qkut pylj"  # Use App Password from Google Account

def generate_otp():
    return str(random.randint(100000, 999999))

def send_otp_email(receiver_email, otp):
    msg = EmailMessage()
    msg['Subject'] = "Your OTP for Login"
    msg['From'] = EMAIL_ADDRESS
    msg['To'] = receiver_email
    msg.set_content(f"Your OTP is: {otp}")

    with smtplib.SMTP_SSL('smtp.gmail.com', 465) as smtp:
        smtp.login(EMAIL_ADDRESS, EMAIL_PASSWORD)
        smtp.send_message(msg)

@app.route('/')
def home():
    return render_template('login.html')

@app.route('/send_otp', methods=['POST'])
def send_otp():
    email = request.form['email']
    otp = generate_otp()
    otp_storage[email] = otp  # Store OTP temporarily

    try:
        send_otp_email(email, otp)
        return render_template('verify.html', email=email)
    except Exception as e:
        return f"Failed to send OTP: {str(e)}"

@app.route('/verify_otp', methods=['POST'])
def verify_otp():
    email = request.form['email']
    user_otp = request.form['otp']

    if email in otp_storage and otp_storage[email] == user_otp:
        del otp_storage[email]  # OTP used, remove from storage
        return render_template('auth_result.html', message="Authentication Successful!", status="success")
    else:
        return render_template('auth_result.html', message="Invalid OTP. Please try again.", status="error")

if __name__ == '__main__':
    app.run(debug=True)