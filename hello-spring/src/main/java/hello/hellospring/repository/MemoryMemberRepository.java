package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;


public class MemoryMemberRepository implements MemberRepository{

    // 동시성 문제 고려 x , 실무에서 사용 시 ConcurrentHashMap, AtomicLong 사용 고려
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        // return store.get(id);
        // get(id)가 null 일 것을 고려하여 Optional 로 감싸줌
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                // 람다식 사용
                // member 에서 member.getName()이 param 으로 넘어온 name 과 equal 인 지 확인
                // 같은 경우에만 filtering 후 찾으면 반환
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); // store.values() = member
    }

    // Test 를 끝낼 때마다 저장된 데이터를 비움
    public void clearStore(){
        store.clear();
    }
}
