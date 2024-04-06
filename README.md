# 密码多功能工具箱



## 开发内容

1、教科书式加密签名方案
1.1、RSA加密
1.2、ElGamal加密
1.3、RSA签名
1.4、应用实例
2、哈希函数
2.1、SM3国产哈希
2.2、同态哈希
2.3、布谷鸟哈希
2.4、Merkle树应用实例
3、秘密分享
3.1、Shamir秘密分享
3.2、双变量多项式秘密分享
3.3、应用实例
3、同态加密
3.1、Paillier加密
3.1.1、基础Paillier加密
3.1.2、引入超递增序列的Paillier加密
3.1.3、分布式Paillier加密
3.2、BGV同态加密
3.3、应用实例
4、数字签名

## 教科书式加密签名方案



### RSA加密

#### 密钥生成

选择两个大素数  $p$ , $q$

计算  $n = p*q$

计算$n$的欧拉函数 $\varphi(n) = (p-1)*(q-1)$

选择一个整数$e$ $( 1<e<\varphi(n),  gcd(e,\varphi(n)) = 1)$

计算 $d \equiv e^{-1}(mod$  $\varphi(n))$

公钥:   $ k_{pub} = (e,n)$

私钥:   $k_{pr} = (d,n)$

#### 密钥分发

   假设Bob想要发送信息给Alice, 决定使用RSA, Bob必须知道Alice的公钥来加密消息 Alice必须用她的私钥来解密消息

为了让Bob传送加密后的消息, Alice把她的公钥$(n,e)$通过一个可靠的, 但不必是秘密的路线传输给Bob


#### 加密

Bob拿到Alice的公钥后, 他可以传输明文$M$给Alice

先将$M$通过一种统一的填充方案变成一个整数$m$ $(0\leq m < n)$​ 

计算密文 $c \equiv m^e (mod $  $n)$

#### 解密

Alice可以用她的私钥d将$c$ 恢复成$m$​

$c^d \equiv {(m^e)}^d \equiv m (mod$  $n)$



### Elgamal加密(基于素数域)

Elgamal加密除了对素数域的乘法群外对其他循环群也适用

#### 密钥生成

选择大素数$p$

选择循环群的生成元$\alpha \in Z_p^* 或Z_p^*的子群$

选择 $d \in \{2,3,...,p-3,p-2\}$

计算 $\beta = \alpha^d$  $mod $  $p$

公钥:  $k_{pub} = (p,\alpha,\beta)$

私钥: $k_{pr} = d$

#### 密钥分发

假设Alice要发送消息给Bob

则Bob需要先将生成的公钥分发给Alice 使Alice能够用Bob的公钥来进行加密

#### 加密

选择 $i \in\{2,3,...,p-3,p-2\}$ 

计算临时密钥 $k_E = \alpha ^ i$ $mod$  $p$

计算掩码密钥 $k_M = \beta ^ i$ $mod$  $p$

加密消息 $m \in Z_p ^ *$    $c = m * k_M$  $mod$  $p$

 #### 解密

Alice加密玩消息后 再将$(k_E, c) 发送给Bob$

Bob先计算掩码密钥 $k_M = k_E ^ d$  $mod$  $p$

最后解密 $m = c * k_M^{-1}$  $mod$  $p$



### RSA签名

#### 密钥生成

同RSA加密

#### 签名

计算消息的哈希值 $h = hash(m)$

用私钥$(d,n)$加密$h$  得到$s = h^d(mod$  $n)$   将$密文s$和明文$m$一起发出

($0\leq h,s<n$)



#### 验证签名

计算明文$m$的哈希值 $h = hash(m)$

对签名用公钥$e$解密 $h' = s^e (mod $  $n)$

比较$h$和$h'$ 若相同则签名有效, 否则签名无效















