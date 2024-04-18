//Ertugrul Sengul

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.*

fun getNumbers(delay: Long): List<Int>{
    val numbers = mutableListOf<Int>()

    for (i in 1..10){
        Thread.sleep(delay)
        numbers.add(i)
    }
    return numbers

}

fun getNumbersFlow(delay: Long): Flow<Int>{
    return flow{
        for (i in 1..10){
            emit(i)
            delay(delay)
        }
    }
}

suspend fun performRequest(request: Int): String{
    delay(1000)
    return "response $request"
}

fun main(args: Array<String>) {

    //Sayfa 6
    CoroutineScope(Dispatchers.IO).launch {
        delay(2000)
        println("2 Saniye geçti")
    }

    //Sayfa 7
    val bootcamp = GlobalScope.async {
        "Getir Android Bootcamp"
    }

    runBlocking {
        delay(1000)
        val message = bootcamp.await()
        println(message)
    }

    //Sayfa 8
    runBlocking {
        delay(3000)
        println("Starting Coroutine with runblocking")
    }

    //Sayfa 9
    // Main içinde main dispatcher kullanırken hata aldım. o yüzden direkt launch kullandım
    CoroutineScope(Dispatchers.IO).launch {
        delay(4000)
        println("I am in ${Thread.currentThread().name}")
    }

    //Sayfa 10
    CoroutineScope(Dispatchers.IO).launch {
        delay(5000) // network simulation
        println("I am in ${Thread.currentThread().name}")
    }

    //Sayfa 11
    CoroutineScope(Dispatchers.Default).launch {
        delay(6000)
        (0..20).forEach {
            if (it % 2 == 0)
                print("$it ")
        }
    }

    //Sayfa 12
    CoroutineScope(Dispatchers.IO).launch {
        delay(7000)
        println()
        var num = 100
        withContext(Dispatchers.Default){
            println("$num")
        }
    }

    //Sayfa 13
    val scope = CoroutineScope(Dispatchers.IO)
    val job = Job()

    scope.launch(job) {
        delay(8000)
        println("Coroutine başlatıldı")
    }
    job.cancel()

    //Sayfa 15
    CoroutineScope(Dispatchers.IO).launch {
        delay(9000)
        print("Print with delay: ")
        val numbers = getNumbers(100)
        for (number in numbers){
            print("$number ")
        }
    }

    //Sayfa 16
    val myFlow = getNumbersFlow(100)
    CoroutineScope(Dispatchers.IO).launch {
        delay(10000)
        print("Print with flow: ")
        myFlow.collect(){ num->
            print("$num ")
        }
        println()
    }

    //Sayfa 17
    val newScope = CoroutineScope(Dispatchers.IO)
    newScope.launch {
        delay(11000)
        println("Hello from new scope")
    }

    //Sayfa 18
    val supervisorJob = SupervisorJob()
    val supervisorJobScope = CoroutineScope(supervisorJob)

    supervisorJobScope.launch {
        delay(12000)
        //this gives error
        //val a = 10 / 0
    }

    supervisorJobScope.launch {
        delay(13000)
        println("0 division detected but this coroutine continues")
    }

    //Sayfa 23
    CoroutineScope(Dispatchers.IO).launch {
        delay(14000)
        val flow = flow<Int> {
            emit(1)
            emit(2)
            emit(3)
        }
        flow.collect(){değer->
            print("$değer ")
        }
        println()
    }

    //Sayfa 24
    CoroutineScope(Dispatchers.IO).launch {
        delay(15000)

        val sayilar = listOf<Int>(1,2,3)
        val flow = sayilar.asFlow()
        flow.collect(){değer->
            print("$değer ")
        }
        println()
    }

    //Sayfa 25
    CoroutineScope(Dispatchers.IO).launch {
        delay(16000)
        val sayilarFlow = flow<Int> {
            emit(1)
            emit(2)
            emit(3)
        }
        val karelarFlow = sayilarFlow.map {sayi->
            sayi*sayi
        }
        karelarFlow.collect(){kare->
            print("$kare ")
        }
        println()
    }

    //Sayfa 26
    CoroutineScope(Dispatchers.IO).launch {
        delay(17000)
        val sayilarFlow = flow<Int> {
            emit(1)
            emit(2)
            emit(3)
            emit(4)
            emit(5)
        }
        val teklerFlow = sayilarFlow.map {sayi->
            sayi % 2 == 1
        }
        teklerFlow.collect(){tek->
            print("$tek ")
        }
        println()
    }

    //Sayfa 27
    CoroutineScope(Dispatchers.IO).launch {
        delay(18000)
        (1..3).asFlow().transform { request->
            emit("Making request $request")
            emit(performRequest(request))
        }.collect{ response->
            print("$response ")
        }
        println()
    }

    //Sayfa 28

    CoroutineScope(Dispatchers.IO).launch {
        delay(19000)
        val sayilarFlow = flow {
            emit(1)
            emit(2)
            emit(3)
            emit(4)
            emit(5)
        }
        sayilarFlow.flowOn(Dispatchers.IO).collect{sayi->
            print("$sayi")
        }
    }

    //Sayfa 31
    val kanal = Channel<Int>()

    CoroutineScope(Dispatchers.IO).launch {
        delay(20000)
        println()
        println("Gönderici Başlatıldı")
        kanal.send(30)
        println("Değer gönderildi: 30")
    }

    CoroutineScope(Dispatchers.IO).launch {
        delay(20000)
        println("Alıcı Başlatıldı")
        val deger = kanal.receive()
        println("Değer alındı: $deger")
    }

    //Sayfa 32
    val unboundedChannel = Channel<String>()

    CoroutineScope(Dispatchers.IO).launch {
        delay(22000)
        println("Sınırsız Kanal Gönderici Başlatıldı")
        for(i in 1..5){
            unboundedChannel.send("Mesaj $i")
            println("Mesaj gönderildi: Mesaj $i")
        }
    }

    CoroutineScope(Dispatchers.IO).launch {
        delay(22000)
        println("Sınırsız Kanal Alıcı Başlatıldı")
        while (true){
            val mesaj = unboundedChannel.receive()
            println("Mesaj alındı: $mesaj")
        }
    }

    //Sayfa 33
    val tickerChannel = ticker(1000, 0)
    CoroutineScope(Dispatchers.IO).launch {
        delay(25000)
        println("Alıcı başlatıldı")
        while (true){
            tickerChannel.receive()
            println("Tıklama Alındı")
        }
    }

    Thread.sleep(30000)
}