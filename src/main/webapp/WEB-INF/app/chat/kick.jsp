<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>


<head>
    <link rel="stylesheet" type="text/css" href="https://unpkg.com/carbon-components/css/carbon-components.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<div class="title">
    <H1 style="font-size:48px;color: red">You are Kicked from Chat</H1>
</div>
<div class="placeholder">

</div>
<div class="section-container" style="padding:15px; !important">
    <div class="section-item">
        <div class="cf-error-details cf-error-522">
            <h1 style="font-size:30px;text-align: left !important;">What happend why i am Kicked?</h1>
            <p class="mb-4" >Look like you are kicked from chat by breaking chat rule please obey chat rules.</p>
            <ul>
                <li>No spamming</li>
                <li>No personal attacks or harassment</li>
                <li>Don't monopolize the conversation</li>
                <li>Be kind with all members</li>
                <li>Don't fight with anyone</li>
            </ul>
        </div>
    </div>

</div>
<style>
    body {
        padding-top: 4rem;
        background-color: #fff;
    }


    /* .cf-error-details {
      max-width: 50%;
      margin: 2rem 0rem;
      text-align: left;
    }

    .cf-error-details > h1 {
      font-size: 2rem;
    }

    .cf-error-details > p {
      margin: .5rem 0rem;
    }

    .cf-error-details > ul {

    } */


    .title {
        margin-bottom: 2rem;
    }

    h1, h2 {
        margin: 0px;
        text-align: center;
    }

    h1 {
        font-size: 6.25rem;
        font-weight: 200;
        color: #152935;
    }

    h2 {
        font-size: 2.5rem;
        font-weight: 100;
        color: #5A6872;
    }

    .placeholder {
        margin-bottom: 1.5rem;
        text-align: center;
        display: flex;
        justify-content: center;
    }

    .section-container {
        background-color: #F5F7FA;
        padding: 3rem 0;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-evenly;
    }

    .section-item {
        max-width: 30rem;
        min-width: 20rem;
    }

    .section-item h1 {
        font-size: 2rem;
        color: #3D70B2;
        font-weight: 300;
        margin-bottom: 1.2rem;
    }

    .section-item p {
        font-size: 1.1rem;
        line-height: 1.6rem;
    }

    .section-item ul {
        margin: 1rem 0rem;
    }

    .section-item li {
        margin: .5rem 0rem;
    }

    #event-specs {
        text-align: center;
        margin-top: 1.5rem;
    }

    @media (max-width: 840px) {
        h1 {
            font-size: 4rem;
        }

        h2 {
            font-size: 2rem;
        }

        .section-item {
            margin-bottom: 3rem;
        }
    }

    @media (max-width: 839px) {
        h1 {
            font-size: 2.5rem;
        }

        h2 {
            font-size: 1rem;
        }

        .section-container {
            padding: 3rem 1rem;
        }

        .section-item {
            margin-bottom: 3rem;
        }

        .section-item > h3 {
            font-size: 1.5rem;
            margin-bottom: .8rem;
        }

        .section-item > p {
            font-size: .8rem;
            line-height: 1.2rem;
        }
    }
</style>
