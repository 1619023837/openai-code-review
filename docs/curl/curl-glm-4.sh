curl -X POST \
        -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsInNpZ25fdHlwZSI6IlNJR04ifQ.eyJhcGlfa2V5IjoiMzc2M2FhMTNhYjI4NDc1MjhkNmZmZGMyZmE2YzUzYzciLCJleHAiOjE3NDQwMjI3OTMzMjIsInRpbWVzdGFtcCI6MTc0NDAyMDk5MzMyOH0.34PXb-Iz-PmTqT1uIwHK1Zi75misg6pawbLHUee9QgA" \
        -H "Content-Type: application/json" \
        -H "User-Agent: Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)" \
        -d '{
          "model":"glm-4",
          "stream": "true",
          "messages": [
              {
                  "role": "user",
                  "content": "1+1"
              }
          ]
        }' \
  https://open.bigmodel.cn/api/paas/v4/chat/completions